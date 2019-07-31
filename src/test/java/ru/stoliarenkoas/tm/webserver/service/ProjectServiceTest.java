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
import ru.stoliarenkoas.tm.webserver.api.service.ProjectServicePageable;
import ru.stoliarenkoas.tm.webserver.enumerate.Role;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.model.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.UserRepositoryPageable;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;

import java.util.List;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader.class,
        classes = {ru.stoliarenkoas.tm.webserver.configuration.JpaConfiguration.class,
                   ProjectServicePageableImpl.class,
                   UserServicePageableImpl.class})
public class ProjectServiceTest {

    private UserRepositoryPageable userRepository;
    @Autowired
    public void setUserRepository(UserRepositoryPageable userRepository) {
        this.userRepository = userRepository;
    }

    private ProjectServicePageable projectService;
    @Autowired
    public void setProjectService(ProjectServicePageable projectService) {
        this.projectService = projectService;
    }

    private final User admin = new User();
    private final User user = new User();

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
        
        user.setId("test-user-id");
        user.setLogin(userLogin);
        user.setPasswordHash(CypherUtil.getMd5(password));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @After
    public void destroy() {
        userRepository.deleteAll();
    }

    @Test
    public void persistTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(user.getId());
        newProject.setName("NewProject");
        projectService.persist(user.getId(), newProject);
        final ProjectDTO persistedProject = projectService.findOne(user.getId(), newProject.getId());
        assertNotNull(persistedProject);
        assertEquals(newProject.getName(), persistedProject.getName());
    }

    @Test(expected = AccessForbiddenException.class)
    public void persistWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(admin.getId());
        newProject.setName("NewProject");
        projectService.persist(user.getId(), newProject);
    }

    @Test(expected = AccessForbiddenException.class)
    public void persistTwiceTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(user.getId());
        newProject.setName("NewProject");
        projectService.persist(user.getId(), newProject);
        projectService.persist(user.getId(), newProject);
    }

    @Test
    public void mergeTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(user.getId());
        newProject.setName("oldName");
        projectService.persist(user.getId(), newProject);
        newProject.setName("newName");
        projectService.merge(user.getId(), newProject);
        final ProjectDTO persistedProject = projectService.findOne(user.getId(), newProject.getId());
        assertNotNull(persistedProject);
        assertEquals(newProject.getName(), persistedProject.getName());
    }

    @Test
    public void mergeNonExistingTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(user.getId());
        newProject.setName("newName");
        projectService.merge(user.getId(), newProject);
        final ProjectDTO persistedProject = projectService.findOne(user.getId(), newProject.getId());
        assertNotNull(persistedProject);
        assertEquals(newProject.getName(), persistedProject.getName());
    }

    @Test(expected = AccessForbiddenException.class)
    public void mergeWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(admin.getId());
        newProject.setName("newName");
        projectService.merge(user.getId(), newProject);
    }

    @Test
    public void findAllTest() throws AccessForbiddenException, IncorrectDataException {
        List<ProjectDTO> adminProjects = projectService.findAllByUserId(admin.getId());
        assertNotNull(adminProjects);
        assertTrue(adminProjects.isEmpty());

        List<ProjectDTO> userProjects = projectService.findAllByUserId(user.getId());
        assertNotNull(userProjects);
        assertTrue(userProjects.isEmpty());

        final int numberOfInputs = 10;
        for (int i = 1; i < numberOfInputs + 1; i++) {
            final ProjectDTO newProject = new ProjectDTO();
            newProject.setUserId(admin.getId());
            newProject.setId(admin.getName() + "-projectid-" + (i));
            newProject.setName(admin.getName() + "-projectName-" + (i));
            projectService.persist(admin.getId(), newProject);

            newProject.setUserId(user.getId());
            newProject.setId(user.getName() + "-projectid-" + (i));
            newProject.setName(user.getName() + "-projectName-" + (i));
            projectService.persist(user.getId(), newProject);
        }

        adminProjects = projectService.findAllByUserId(admin.getId());
        assertNotNull(adminProjects);
        assertEquals(numberOfInputs, adminProjects.size());

        userProjects = projectService.findAllByUserId(user.getId());
        assertNotNull(userProjects);
        assertEquals(numberOfInputs, userProjects.size());
    }

    @Test
    public void existsTest() throws AccessForbiddenException, IncorrectDataException {
        final String adminProjectId = "adminProjectId";
        final ProjectDTO adminProject = new ProjectDTO();
        adminProject.setId(adminProjectId);
        adminProject.setUserId(admin.getId());
        adminProject.setName("adminProject");

        final String userProjectId = "userProjectId";
        final ProjectDTO userProject = new ProjectDTO();
        userProject.setId(userProjectId);
        userProject.setUserId(user.getId());
        userProject.setName("userProject");

        projectService.persist(user.getId(), userProject);
        projectService.persist(admin.getId(), adminProject);

        assertTrue(projectService.exists(admin.getId(), adminProjectId));
        assertTrue(projectService.exists(user.getId(), userProjectId));

        assertFalse(projectService.exists(admin.getId(), userProjectId));
        assertFalse(projectService.exists(user.getId(), adminProjectId));
    }

    @Test
    public void removeTest() throws AccessForbiddenException, IncorrectDataException {
        final String userProjectId = "userProjectId";
        final ProjectDTO userProject = new ProjectDTO();
        userProject.setId(userProjectId);
        userProject.setUserId(user.getId());
        userProject.setName("userProject");

        projectService.persist(user.getId(), userProject);
        projectService.remove(user.getId(), userProjectId);
        assertFalse(projectService.exists(user.getId(), userProjectId));
    }


    @Test(expected = IncorrectDataException.class)
    public void removeWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final String userProjectId = "userProjectId";
        final ProjectDTO userProject = new ProjectDTO();
        userProject.setId(userProjectId);
        userProject.setUserId(user.getId());
        userProject.setName("userProject");

        projectService.persist(user.getId(), userProject);
        projectService.remove(admin.getId(), userProjectId);
    }

}
