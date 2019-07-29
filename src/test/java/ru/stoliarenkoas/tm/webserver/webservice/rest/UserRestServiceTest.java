package ru.stoliarenkoas.tm.webserver.webservice.rest;


import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.stoliarenkoas.tm.webserver.DataConstants;
import ru.stoliarenkoas.tm.webserver.DataGenerator;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.webservice.rest.client.UserRestServiceClient;
import ru.stoliarenkoas.tm.webserver.webservice.rest.resource.UserRestResourceProvider;

import static org.junit.Assert.assertNotNull;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader.class,
        classes = {
                DataGenerator.class,
                UserRestResourceProvider.class,
                ru.stoliarenkoas.tm.webserver.configuration.JpaConfiguration.class,
                ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.webservice.rest.UserRestService.class,
                ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider.class})
public class UserRestServiceTest {

    private Server userRestServer;
    @Autowired
    public void setUserRestServer(Server userRestServer) {
        this.userRestServer = userRestServer;
    }

    private UserRestServiceClient client;
    @Autowired
    public void setClient(UserRestServiceClient client) {
        this.client = client;
    }

    @Test
    public void loginTest() {
        final String token = client.login(DataConstants.ADMIN_LOGIN, DataConstants.PASSWORD);
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
//        final UserDTO admin = userRestService.getOneUser()
    }

}
