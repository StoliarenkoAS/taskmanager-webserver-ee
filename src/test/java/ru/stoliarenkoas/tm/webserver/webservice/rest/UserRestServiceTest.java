package ru.stoliarenkoas.tm.webserver.webservice.rest;


import org.apache.cxf.endpoint.Server;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.stoliarenkoas.tm.webserver.DataConstants;
import ru.stoliarenkoas.tm.webserver.DataGenerator;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.webservice.rest.client.UserRestServiceClient;
import ru.stoliarenkoas.tm.webserver.webservice.rest.resource.ProjectRestResourceProvider;
import ru.stoliarenkoas.tm.webserver.webservice.rest.resource.TaskRestResourceProvider;
import ru.stoliarenkoas.tm.webserver.webservice.rest.resource.UserRestResourceProvider;

import java.util.List;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {
                DataGenerator.class,
                UserRestService.class,
                ProjectRestService.class,
                TaskRestService.class,
                UserRestResourceProvider.class,
                ProjectRestResourceProvider.class,
                TaskRestResourceProvider.class,
                ru.stoliarenkoas.tm.webserver.configuration.JpaConfiguration.class,
                ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.service.ProjectServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.service.TaskServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider.class})
public class UserRestServiceTest {

    @Autowired
    private String adminToken;

    @Autowired
    private String userToken;

    private UserRestServiceClient client;
    @Autowired
    public void setClient(UserRestServiceClient client) {
        this.client = client;
    }

    @AfterClass
    public static void stopEndpoint() {
    }

    @Test
    public void loginTest() {
        final String token = client.login(DataConstants.ADMIN_LOGIN, DataConstants.PASSWORD);
        System.out.println(token);
        assertNotNull(token);
    }

    @Test
    public void registerTest() {
        final String newUserLogin = "new-user-login";
        final String newUserPassword = "new-user-password";
        client.userRegister(newUserLogin, newUserPassword);
        final String token = UserRestServiceClient.client().login(newUserLogin, newUserPassword);
        assertNotNull(token);
    }

    @Test
    public void getOneTest() {
        final UserDTO user = client.getOneUser(adminToken, DataConstants.USER_ID);
        assertNotNull(user);
        assertEquals(DataConstants.USER_LOGIN, user.getLogin());
    }

    @Test(expected = feign.RetryableException.class)
    public void getOneWrongRoleTest() {
        client.getOneUser(userToken, DataConstants.ADMIN_ID);
    }

    @Test
    public void getSelfTest() {
        final UserDTO user = client.getOneUser(userToken, DataConstants.USER_ID);
        assertNotNull(user);
        assertEquals(DataConstants.USER_LOGIN, user.getLogin());
    }

    @Test
    public void getAllTest() {
        final List<UserDTO> users = client.getAllUsers(adminToken);
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test(expected = feign.RetryableException.class)
    public void getAllWrongRoleTest() {
        final List<UserDTO> users = client.getAllUsers(userToken);
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

}
