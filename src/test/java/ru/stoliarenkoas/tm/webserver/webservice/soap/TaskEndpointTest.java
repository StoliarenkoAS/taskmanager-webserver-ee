package ru.stoliarenkoas.tm.webserver.webservice.soap;

import org.apache.cxf.endpoint.Server;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.stoliarenkoas.tm.webserver.DataConstants;
import ru.stoliarenkoas.tm.webserver.api.websevice.soap.TaskEndpoint;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;
import ru.stoliarenkoas.tm.webserver.webservice.soap.resource.ProjectSoapResourceProvider;
import ru.stoliarenkoas.tm.webserver.webservice.soap.resource.TaskSoapResourceProvider;
import ru.stoliarenkoas.tm.webserver.webservice.soap.resource.UserSoapResourceProvider;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader.class,
        classes = {
                UserEndpointImpl.class,
                ProjectEndpointImpl.class,
                TaskEndpointImpl.class,
                UserSoapResourceProvider.class,
                ProjectSoapResourceProvider.class,
                TaskSoapResourceProvider.class,
                ru.stoliarenkoas.tm.webserver.DataGenerator.class,
                ru.stoliarenkoas.tm.webserver.configuration.JpaConfiguration.class,
                ru.stoliarenkoas.tm.webserver.service.TaskServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.service.ProjectServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider.class
        })
public class TaskEndpointTest {

    private final String testTaskLogin = "soap-test-task-login";

    @Autowired
    private String userToken;

    @Autowired
    private String adminToken;

    private TaskEndpointImpl taskEndpoint;
    @Autowired
    public void setTaskEndpoint(TaskEndpointImpl taskEndpoint) {
        this.taskEndpoint = taskEndpoint;
    }

    private Server taskEndpointServer;
    @Autowired
    public void setTaskEndpointServer(Server taskEndpointServer) {
        this.taskEndpointServer = taskEndpointServer;
    }

    private TaskEndpoint taskEndpointClient;
    @Autowired
    public void setTaskEndpointClient(TaskEndpoint taskEndpointClient) {
        this.taskEndpointClient = taskEndpointClient;
    }

    @After
    public void destroy() {
        System.out.println();
    }

    @Test
    public void getOneTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO taskDTO = taskEndpoint.getOneTask(userToken, DataConstants.SINGLE_TASK_ID);
        assertNotNull(taskDTO);
    }

    @Test
    public void getOneWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO taskDTO = taskEndpoint.getOneTask(adminToken, DataConstants.SINGLE_TASK_ID);
        assertNull(taskDTO);
    }

    @Test(expected = AccessForbiddenException.class)
    public void getOneUnauthorizedTest() throws AccessForbiddenException, IncorrectDataException {
        taskEndpoint.getOneTask(null, DataConstants.SINGLE_TASK_ID);
    }

    @Test
    public void getAllTest() throws AccessForbiddenException {
        final List<TaskDTO> adminsTasks = taskEndpoint.getAllTasks(adminToken);
        assertNotNull(adminsTasks);

        final List<TaskDTO> usersTasks = taskEndpoint.getAllTasks(userToken);
        assertNotNull(usersTasks);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAllUnauthorizedTest() throws AccessForbiddenException {
        taskEndpoint.getAllTasks("");
    }

    @Test(expected = AccessForbiddenException.class)
    public void persistTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(DataConstants.ADMIN_ID);
        newTask.setProjectId(DataConstants.TWO_TASKS_PROJECT_ID);
        newTask.setName("new-task-to-persist");
        taskEndpoint.persistTask(adminToken, newTask);
        final TaskDTO persistedTask = taskEndpoint.getOneTask(adminToken, newTask.getId());
        assertNotNull(persistedTask);
        taskEndpoint.persistTask(adminToken, persistedTask);
    }

    @Test(expected = AccessForbiddenException.class)
    public void persistWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(DataConstants.ADMIN_ID);
        newTask.setProjectId(DataConstants.TWO_TASKS_PROJECT_ID);
        newTask.setName("new-task-to-persist");
        taskEndpoint.persistTask(userToken, newTask);
    }

    @Test
    public void mergeTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(DataConstants.ADMIN_ID);
        newTask.setProjectId(DataConstants.TWO_TASKS_PROJECT_ID);
        newTask.setName("new-task-to-merge");
        taskEndpoint.mergeTask(adminToken, newTask);
        taskEndpoint.mergeTask(adminToken, newTask);
        final TaskDTO persistedTask = taskEndpoint.getOneTask(adminToken, newTask.getId());
        assertNotNull(persistedTask);
    }

    @Test(expected = AccessForbiddenException.class)
    public void mergeWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(DataConstants.USER_ID);
        newTask.setProjectId(DataConstants.TWO_TASKS_PROJECT_ID);
        newTask.setName("new-task-to-merge");
        taskEndpoint.mergeTask(adminToken, newTask);
    }

    @Test
    public void deleteTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(DataConstants.ADMIN_ID);
        newTask.setProjectId(DataConstants.TWO_TASKS_PROJECT_ID);
        newTask.setName("new-task-to-delete");
        taskEndpoint.persistTask(adminToken, newTask);
        final TaskDTO persistedTask = taskEndpoint.getOneTask(adminToken, newTask.getId());
        assertNotNull(persistedTask);
        taskEndpoint.deleteOneTask(adminToken, newTask.getId());
        final TaskDTO deletedTask = taskEndpoint.getOneTask(adminToken, newTask.getId());
        assertNull(deletedTask);
    }

    @Test(expected = IncorrectDataException.class)
    public void deleteWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(DataConstants.ADMIN_ID);
        newTask.setProjectId(DataConstants.TWO_TASKS_PROJECT_ID);
        newTask.setName("new-task-to-delete");
        taskEndpoint.persistTask(adminToken, newTask);
        final TaskDTO persistedTask = taskEndpoint.getOneTask(adminToken, newTask.getId());
        assertNotNull(persistedTask);
        taskEndpoint.deleteOneTask(userToken, newTask.getId());
    }

}
