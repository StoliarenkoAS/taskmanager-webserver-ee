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
import ru.stoliarenkoas.tm.webserver.configuration.JpaConfiguration;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.service.ProjectServicePageableImpl;
import ru.stoliarenkoas.tm.webserver.service.TaskServicePageableImpl;
import ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl;
import ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider;
import ru.stoliarenkoas.tm.webserver.webservice.rest.client.ProjectRestServiceClient;
import ru.stoliarenkoas.tm.webserver.webservice.rest.resource.ProjectRestResourceProvider;
import ru.stoliarenkoas.tm.webserver.webservice.rest.resource.TaskRestResourceProvider;
import ru.stoliarenkoas.tm.webserver.webservice.rest.resource.UserRestResourceProvider;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {
                DataGenerator.class,
                JpaConfiguration.class,
                JwtTokenProvider.class,
                UserRestService.class,
                TaskRestService.class,
                ProjectRestService.class,
                UserServicePageableImpl.class,
                TaskServicePageableImpl.class,
                ProjectServicePageableImpl.class,
                UserRestResourceProvider.class,
                TaskRestResourceProvider.class,
                ProjectRestResourceProvider.class,
        })
public class ProjectRestServiceTest {

    @Autowired
    private String adminToken;

    @Autowired
    private String userToken;

    private ProjectRestServiceClient client;
    @Autowired
    public void setClient(ProjectRestServiceClient client) {
        this.client = client;
    }

    @Test
    public void getOneTest() {
        final ProjectDTO project = client.getOneProject(adminToken, DataConstants.TWO_TASKS_PROJECT_ID);
        assertNotNull(project);
    }

    @Test
    public void getOneWrongRoleTest() {
        final ProjectDTO project = client.getOneProject(userToken, DataConstants.TWO_TASKS_PROJECT_ID);
        assertNull(project);
    }

    @Test
    public void getAllTest() {
        final List<ProjectDTO> projects = client.getAllProjects(userToken);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
    }

    @Test
    public void persistTest() throws IOException, AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(DataConstants.USER_ID);
        newProject.setName("new-rest-project");
        client.persistProject(userToken, newProject);

        final ProjectDTO persistedProject = client.getOneProject(userToken, newProject.getId());
        assertNotNull(persistedProject);
        assertEquals(newProject.getName(), persistedProject.getName());
    }

    @Test(expected = feign.RetryableException.class)
    public void persistTwiceTest() throws IOException, AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(DataConstants.USER_ID);
        newProject.setName("twice-persisted-rest-project");
        client.persistProject(userToken, newProject);
        client.persistProject(userToken, newProject);
    }

    @Test
    public void deleteTest() throws IOException, IncorrectDataException, AccessForbiddenException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(DataConstants.USER_ID);
        newProject.setName("deletable-rest-project");
        client.persistProject(userToken, newProject);
        final ProjectDTO persistedProject = client.getOneProject(userToken, newProject.getId());
        assertNotNull(persistedProject);

        client.deleteOneProject(userToken, newProject.getId());
        final ProjectDTO persistedProjectAfterDeletion = client.getOneProject(userToken, newProject.getId());
        assertNull(persistedProjectAfterDeletion);
    }

}
