package ru.stoliarenkoas.tm.webserver.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.stoliarenkoas.tm.webserver.api.service.TaskServicePageable;
import ru.stoliarenkoas.tm.webserver.enumerate.Role;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.model.entity.Project;
import ru.stoliarenkoas.tm.webserver.model.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.ProjectRepositoryPageable;
import ru.stoliarenkoas.tm.webserver.repository.UserRepositoryPageable;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;

import java.util.List;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader.class,
        classes = {ru.stoliarenkoas.tm.webserver.configuration.JpaConfiguration.class,
                   TaskServicePageableImpl.class,
                   ProjectServicePageableImpl.class,
                   UserServicePageableImpl.class})
public class TaskServiceTest {

    private UserRepositoryPageable userRepository;
    @Autowired
    public void setUserRepository(UserRepositoryPageable userRepository) {
        this.userRepository = userRepository;
    }

    private ProjectRepositoryPageable projectRepository;
    @Autowired
    public void setProjectRepository(ProjectRepositoryPageable projectRepository) {
        this.projectRepository = projectRepository;
    }

    private TaskServicePageable taskService;
    @Autowired
    public void setTaskService(TaskServicePageable taskService) {
        this.taskService = taskService;
    }

    private final User admin = new User();
    private final User user = new User();
    private final Project adminProject = new Project();
    private final Project userProject = new Project();

    @Before
    public void init() {
        final String adminLogin = "adminLogin";
        final String userLogin = "userLogin";
        final String password = "password";

        admin.setId("test-admin-id");
        admin.setLogin(adminLogin);
        admin.setPasswordHash(CypherUtil.getMd5(password));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        adminProject.setUser(admin);
        adminProject.setId("test-admin-project-id");
        adminProject.setName("admin-project-name");
        projectRepository.save(adminProject);
        
        user.setId("test-user-id");
        user.setLogin(userLogin);
        user.setPasswordHash(CypherUtil.getMd5(password));
        user.setRole(Role.USER);
        userRepository.save(user);
        userProject.setUser(user);
        userProject.setId("test-user-project-id");
        userProject.setName("user-project-name");
        projectRepository.save(userProject);
    }

    @After
    public void destroy() {
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void persistTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(user.getId());
        newTask.setProjectId(userProject.getId());
        newTask.setName("NewTask");
        taskService.persist(user.getId(), newTask);
        final TaskDTO persistedTask = taskService.findOne(user.getId(), newTask.getId());
        assertNotNull(persistedTask);
        assertEquals(newTask.getName(), persistedTask.getName());
    }

    @Test(expected = AccessForbiddenException.class)
    public void persistWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(admin.getId());
        newTask.setProjectId(adminProject.getId());
        newTask.setName("NewTask");
        taskService.persist(user.getId(), newTask);
    }

    @Test(expected = AccessForbiddenException.class)
    public void persistTwiceTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(user.getId());
        newTask.setProjectId(userProject.getId());
        newTask.setName("NewTask");
        taskService.persist(user.getId(), newTask);
        taskService.persist(user.getId(), newTask);
    }

    @Test
    public void mergeTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(user.getId());
        newTask.setProjectId(userProject.getId());
        newTask.setName("oldName");
        taskService.persist(user.getId(), newTask);
        newTask.setName("newName");
        taskService.merge(user.getId(), newTask);
        final TaskDTO persistedTask = taskService.findOne(user.getId(), newTask.getId());
        assertNotNull(persistedTask);
        assertEquals(newTask.getName(), persistedTask.getName());
    }

    @Test
    public void mergeNonExistingTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(user.getId());
        newTask.setProjectId(userProject.getId());
        newTask.setName("newName");
        taskService.merge(user.getId(), newTask);
        final TaskDTO persistedTask = taskService.findOne(user.getId(), newTask.getId());
        assertNotNull(persistedTask);
        assertEquals(newTask.getName(), persistedTask.getName());
    }

    @Test(expected = AccessForbiddenException.class)
    public void mergeWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(admin.getId());
        newTask.setProjectId(adminProject.getId());
        newTask.setName("newName");
        taskService.merge(user.getId(), newTask);
    }

    @Test
    public void findAllTest() throws AccessForbiddenException, IncorrectDataException {
        List<TaskDTO> adminTasks = taskService.findAllByUserId(admin.getId());
        assertNotNull(adminTasks);
        assertTrue(adminTasks.isEmpty());

        List<TaskDTO> userTasks = taskService.findAllByUserId(user.getId());
        assertNotNull(userTasks);
        assertTrue(userTasks.isEmpty());

        final int numberOfInputs = 10;
        for (int i = 1; i < numberOfInputs + 1; i++) {
            final TaskDTO newTask = new TaskDTO();
            newTask.setUserId(admin.getId());
            newTask.setProjectId(adminProject.getId());
            newTask.setId(admin.getName() + "-taskid-" + (i));
            newTask.setName(admin.getName() + "-taskName-" + (i));
            taskService.persist(admin.getId(), newTask);

            newTask.setUserId(user.getId());
            newTask.setProjectId(userProject.getId());
            newTask.setId(user.getName() + "-taskid-" + (i));
            newTask.setName(user.getName() + "-taskName-" + (i));
            taskService.persist(user.getId(), newTask);
        }

        adminTasks = taskService.findAllByUserId(admin.getId());
        assertNotNull(adminTasks);
        assertEquals(numberOfInputs, adminTasks.size());

        userTasks = taskService.findAllByUserId(user.getId());
        assertNotNull(userTasks);
        assertEquals(numberOfInputs, userTasks.size());
    }

    @Test
    public void existsTest() throws AccessForbiddenException, IncorrectDataException {
        final String adminTaskId = "adminTaskId";
        final TaskDTO adminTask = new TaskDTO();
        adminTask.setId(adminTaskId);
        adminTask.setProjectId(adminProject.getId());
        adminTask.setUserId(admin.getId());
        adminTask.setName("adminTask");

        final String userTaskId = "userTaskId";
        final TaskDTO userTask = new TaskDTO();
        userTask.setId(userTaskId);
        userTask.setProjectId(userProject.getId());
        userTask.setUserId(user.getId());
        userTask.setName("userTask");

        taskService.persist(user.getId(), userTask);
        taskService.persist(admin.getId(), adminTask);

        assertTrue(taskService.exists(admin.getId(), adminTaskId));
        assertTrue(taskService.exists(user.getId(), userTaskId));

        assertFalse(taskService.exists(admin.getId(), userTaskId));
        assertFalse(taskService.exists(user.getId(), adminTaskId));
    }

    @Test
    public void removeTest() throws AccessForbiddenException, IncorrectDataException {
        final String userTaskId = "userTaskId";
        final TaskDTO userTask = new TaskDTO();
        userTask.setId(userTaskId);
        userTask.setProjectId(userProject.getId());
        userTask.setUserId(user.getId());
        userTask.setName("userTask");

        taskService.persist(user.getId(), userTask);
        taskService.remove(user.getId(), userTaskId);
        assertFalse(taskService.exists(user.getId(), userTaskId));
    }


    @Test(expected = IncorrectDataException.class)
    public void removeWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final String userTaskId = "userTaskId";
        final TaskDTO userTask = new TaskDTO();
        userTask.setId(userTaskId);
        userTask.setProjectId(userProject.getId());
        userTask.setUserId(user.getId());
        userTask.setName("userTask");

        taskService.persist(user.getId(), userTask);
        taskService.remove(admin.getId(), userTaskId);
    }

}
