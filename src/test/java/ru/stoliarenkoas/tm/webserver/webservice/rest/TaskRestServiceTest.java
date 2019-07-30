package ru.stoliarenkoas.tm.webserver.webservice.rest;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.stoliarenkoas.tm.webserver.DataConstants;
import ru.stoliarenkoas.tm.webserver.DataGenerator;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;
import ru.stoliarenkoas.tm.webserver.webservice.rest.client.TaskRestServiceClient;
import ru.stoliarenkoas.tm.webserver.webservice.rest.resource.TaskRestResourceProvider;
import ru.stoliarenkoas.tm.webserver.webservice.rest.resource.UserRestResourceProvider;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader.class,
        classes = {
                DataGenerator.class,
                UserRestService.class,
                ProjectRestService.class,
                TaskRestService.class,
                UserRestResourceProvider.class,
                TaskRestResourceProvider.class,
                ru.stoliarenkoas.tm.webserver.configuration.JpaConfiguration.class,
                ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.service.ProjectServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.service.TaskServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider.class})
public class TaskRestServiceTest {

    @Autowired
    private String adminToken;

    @Autowired
    private String userToken;

    private TaskRestServiceClient client;
    @Autowired
    public void setClient(TaskRestServiceClient client) {
        this.client = client;
    }

    @Test
    public void getOneTest() {
        final TaskDTO task = client.getOneTask(adminToken, DataConstants.FIRST_OF_TWO_TASKS_ID);
        assertNotNull(task);
    }

    @Test
    public void getOneWrongRoleTest() {
        final TaskDTO task = client.getOneTask(adminToken, DataConstants.SINGLE_TASK_ID);
        assertNull(task);
    }

    @Test
    public void getAllTest() {
        final List<TaskDTO> tasks = client.getAllTasks(userToken);
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
    }

    @Test
    public void persistTest() throws IOException, AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(DataConstants.USER_ID);
        newTask.setProjectId(DataConstants.ONE_TASK_PROJECT_ID);
        newTask.setName("new-rest-task");
        client.persistTask(userToken, newTask);

        final TaskDTO persistedTask = client.getOneTask(userToken, newTask.getId());
        assertNotNull(persistedTask);
        assertEquals(newTask.getName(), persistedTask.getName());
    }

    @Test(expected = feign.RetryableException.class)
    public void persistTwiceTest() throws IOException, AccessForbiddenException, IncorrectDataException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(DataConstants.USER_ID);
        newTask.setProjectId(DataConstants.ONE_TASK_PROJECT_ID);
        newTask.setName("twice-persisted-rest-task");
        client.persistTask(userToken, newTask);
        client.persistTask(userToken, newTask);
    }

    @Test
    public void deleteTest() throws IOException, IncorrectDataException, AccessForbiddenException {
        final TaskDTO newTask = new TaskDTO();
        newTask.setUserId(DataConstants.USER_ID);
        newTask.setProjectId(DataConstants.ONE_TASK_PROJECT_ID);
        newTask.setName("deletable-rest-task");
        client.persistTask(userToken, newTask);
        final TaskDTO persistedTask = client.getOneTask(userToken, newTask.getId());
        assertNotNull(persistedTask);
        
        client.deleteOneTask(userToken, newTask.getId());
        final TaskDTO persistedTaskAfterDeletion = client.getOneTask(userToken, newTask.getId());
        assertNull(persistedTaskAfterDeletion);
    }

}
