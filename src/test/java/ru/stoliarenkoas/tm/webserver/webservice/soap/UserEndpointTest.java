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
import ru.stoliarenkoas.tm.webserver.api.websevice.soap.UserEndpoint;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.webservice.soap.resource.UserSoapResourceProvider;

import java.util.List;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader.class,
        classes = {
                UserEndpointImpl.class,
                UserSoapResourceProvider.class,
                ru.stoliarenkoas.tm.webserver.DataGenerator.class,
                ru.stoliarenkoas.tm.webserver.configuration.JpaConfiguration.class,
                ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider.class
        })
public class UserEndpointTest {

    private final String testUserLogin = "soap-test-user-login";

    private UserEndpointImpl userEndpoint;
    @Autowired
    public void setUserEndpoint(UserEndpointImpl userEndpoint) {
        this.userEndpoint = userEndpoint;
    }

    private Server userEndpointServer;
    @Autowired
    public void setUserEndpointServer(Server userEndpointServer) {
        this.userEndpointServer = userEndpointServer;
    }

    private UserEndpoint userEndpointClient;
    @Autowired
    public void setUserEndpointClient(UserEndpoint userEndpointClient) {
        this.userEndpointClient = userEndpointClient;
    }

    @After
    public void destroy() {
        System.out.println();
    }

    @Test
    public void loginTest() throws IncorrectDataException {
        final String userToken = userEndpoint.userLogin(DataConstants.USER_LOGIN, DataConstants.PASSWORD);
        assertNotNull(userToken);
        final String adminToken = userEndpoint.userLogin(DataConstants.ADMIN_LOGIN, DataConstants.PASSWORD);
        assertNotNull(adminToken);
        assertNotEquals(userToken, adminToken);
    }

    @Test
    public void registerTest() throws IncorrectDataException {
        userEndpoint.userRegister(testUserLogin, DataConstants.PASSWORD);
        final String testUserToken = userEndpoint.userLogin(testUserLogin, DataConstants.PASSWORD);
        assertNotNull(testUserToken);
        assertFalse(testUserToken.isEmpty());
    }

    @Test
    public void getAllTest() throws IncorrectDataException, AccessForbiddenException {
        final String token = userEndpoint.userLogin(DataConstants.ADMIN_LOGIN, DataConstants.PASSWORD);
        assertNotNull(token);
        final List<UserDTO> userList = userEndpoint.getAllUsers(token);
        assertNotNull(userList);
        assertFalse(userList.isEmpty());
    }

    @Test(expected = AccessForbiddenException.class)
    public void getAllWrongRoleTest() throws IncorrectDataException, AccessForbiddenException {
        final String token = userEndpoint.userLogin(DataConstants.USER_LOGIN, DataConstants.PASSWORD);
        assertNotNull(token);
        final List<UserDTO> userList = userEndpoint.getAllUsers(token);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAllUnauthorizedTest() throws IncorrectDataException, AccessForbiddenException {
        final List<UserDTO> userList = userEndpoint.getAllUsers("");
    }

    @Test
    public void getOneTest() throws IncorrectDataException, AccessForbiddenException {
        final String token = userEndpoint.userLogin(DataConstants.ADMIN_LOGIN, DataConstants.PASSWORD);
        assertNotNull(token);
        final UserDTO userDTO = userEndpoint.getOneUser(token, DataConstants.USER_ID);
        assertNotNull(userDTO);
        assertEquals(DataConstants.USER_LOGIN, userDTO.getLogin());
    }

    @Test(expected = AccessForbiddenException.class)
    public void getOneWrongRoleTest() throws IncorrectDataException, AccessForbiddenException {
        final String token = userEndpoint.userLogin(DataConstants.USER_LOGIN, DataConstants.PASSWORD);
        assertNotNull(token);
        final UserDTO userDTO = userEndpoint.getOneUser(token, DataConstants.ADMIN_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOneUnauthorizedTest() throws IncorrectDataException, AccessForbiddenException {
        final UserDTO userDTO = userEndpoint.getOneUser("", DataConstants.USER_ID);
    }

    @Test
    public void getSelfTest() throws IncorrectDataException, AccessForbiddenException {
        final String token = userEndpoint.userLogin(DataConstants.USER_LOGIN, DataConstants.PASSWORD);
        assertNotNull(token);
        final UserDTO userDTO = userEndpoint.getOneUser(token, DataConstants.USER_ID);
    }

    @Test
    public void removeTest() throws IncorrectDataException, AccessForbiddenException {
        final String token = userEndpoint.userLogin(DataConstants.ADMIN_LOGIN, DataConstants.PASSWORD);
        assertNotNull(token);
        userEndpoint.deleteOneUser(token, DataConstants.USER_TO_DELETE_ID);
        final UserDTO deletedUser = userEndpoint.getOneUser(token, DataConstants.USER_TO_DELETE_ID);
        assertNull(deletedUser);
    }

    @Test(expected = AccessForbiddenException.class)
    public void removeWrongRoleTest() throws IncorrectDataException, AccessForbiddenException {
        final String token = userEndpoint.userLogin(DataConstants.USER_LOGIN, DataConstants.PASSWORD);
        assertNotNull(token);
        userEndpoint.deleteOneUser(token, DataConstants.USER_TO_DELETE_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeUnauthorizedTest() throws IncorrectDataException, AccessForbiddenException {
        userEndpoint.deleteOneUser("", DataConstants.USER_TO_DELETE_ID);
    }

}
