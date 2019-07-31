package ru.stoliarenkoas.tm.webserver.webservice.soap;

import org.apache.cxf.endpoint.Server;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.stoliarenkoas.tm.webserver.DataConstants;
import ru.stoliarenkoas.tm.webserver.api.websevice.soap.ProjectEndpoint;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.webservice.soap.resource.ProjectSoapResourceProvider;
import ru.stoliarenkoas.tm.webserver.webservice.soap.resource.TaskSoapResourceProvider;
import ru.stoliarenkoas.tm.webserver.webservice.soap.resource.UserSoapResourceProvider;

import java.util.List;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
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
public class ProjectEndpointTest {

    private final String testProjectLogin = "soap-test-project-login";

    @Autowired
    private String userToken;

    @Autowired
    private String adminToken;

    private ProjectEndpointImpl projectEndpoint;
    @Autowired
    public void setProjectEndpoint(ProjectEndpointImpl projectEndpoint) {
        this.projectEndpoint = projectEndpoint;
    }

    private Server projectEndpointServer;
    @Autowired
    public void setProjectEndpointServer(Server projectEndpointServer) {
        this.projectEndpointServer = projectEndpointServer;
    }

    private ProjectEndpoint projectEndpointClient;
    @Autowired
    public void setProjectEndpointClient(ProjectEndpoint projectEndpointClient) {
        this.projectEndpointClient = projectEndpointClient;
    }

    @After
    public void destroy() {
        System.out.println();
    }

    @Test
    public void getOneTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO projectDTO = projectEndpoint.getOneProject(userToken, DataConstants.ONE_TASK_PROJECT_ID);
        assertNotNull(projectDTO);
    }

    @Test
    public void getOneWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO projectDTO = projectEndpoint.getOneProject(adminToken, DataConstants.ONE_TASK_PROJECT_ID);
        assertNull(projectDTO);
    }

    @Test(expected = AccessForbiddenException.class)
    public void getOneUnauthorizedTest() throws AccessForbiddenException, IncorrectDataException {
        projectEndpoint.getOneProject(null, DataConstants.ONE_TASK_PROJECT_ID);
    }

    @Test
    public void getAllTest() throws AccessForbiddenException {
        final List<ProjectDTO> adminsProjects = projectEndpoint.getAllProjects(adminToken);
        assertNotNull(adminsProjects);

        final List<ProjectDTO> usersProjects = projectEndpoint.getAllProjects(userToken);
        assertNotNull(usersProjects);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAllUnauthorizedTest() throws AccessForbiddenException {
        projectEndpoint.getAllProjects("");
    }

    @Test(expected = AccessForbiddenException.class)
    public void persistTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(DataConstants.ADMIN_ID);
        newProject.setName("new-project-to-persist");
        projectEndpoint.persistProject(adminToken, newProject);
        final ProjectDTO persistedProject = projectEndpoint.getOneProject(adminToken, newProject.getId());
        assertNotNull(persistedProject);
        projectEndpoint.persistProject(adminToken, persistedProject);
    }

    @Test(expected = AccessForbiddenException.class)
    public void persistWrongUserTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(DataConstants.ADMIN_ID);
        newProject.setName("new-project-to-persist");
        projectEndpoint.persistProject(userToken, newProject);
    }

    @Test
    public void mergeTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(DataConstants.ADMIN_ID);
        newProject.setName("new-project-to-merge");
        projectEndpoint.mergeProject(adminToken, newProject);
        projectEndpoint.mergeProject(adminToken, newProject);
        final ProjectDTO persistedProject = projectEndpoint.getOneProject(adminToken, newProject.getId());
        assertNotNull(persistedProject);
    }

    @Test
    public void deleteTest() throws AccessForbiddenException, IncorrectDataException {
        final ProjectDTO newProject = new ProjectDTO();
        newProject.setUserId(DataConstants.ADMIN_ID);
        newProject.setName("new-project-to-delete");
        projectEndpoint.persistProject(adminToken, newProject);
        final ProjectDTO persistedProject = projectEndpoint.getOneProject(adminToken, newProject.getId());
        assertNotNull(persistedProject);
        projectEndpoint.deleteOneProject(adminToken, newProject.getId());
        final ProjectDTO deletedProject = projectEndpoint.getOneProject(adminToken, newProject.getId());
        assertNull(deletedProject);
    }

}
