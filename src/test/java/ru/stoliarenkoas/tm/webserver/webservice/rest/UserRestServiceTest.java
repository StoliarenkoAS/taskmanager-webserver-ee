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
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.model.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.UserRepositoryPageable;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;
import ru.stoliarenkoas.tm.webserver.webservice.rest.client.UserRestServiceClient;

import static org.junit.Assert.assertNotNull;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader.class,
        classes = {
                ru.stoliarenkoas.tm.webserver.configuration.JpaConfiguration.class,
                ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.webservice.rest.UserRestService.class,
                ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider.class})
public class UserRestServiceTest {

    private Server server;
    final String address = "http://localhost:8080/webservice/rs/user";

    private final String adminLogin = "adminLogin";
    private final String userLogin = "userLogin";
    private final String password = "password";
    private final User admin = new User();
    private final User user = new User();

    private UserRestService userRestService;
    @Autowired
    public void setUserRestService(UserRestService userRestService) {
        this.userRestService = userRestService;
    }

    private UserRepositoryPageable userRepository;
    @Autowired
    public void setUserRepository(UserRepositoryPageable userRepository) {
        this.userRepository = userRepository;
    }

    @Before
    public void init() {
        initUsers();
        startServer();
    }

    private void initUsers() {
        admin.setId("test-admin-id");
        admin.setLogin(adminLogin);
        admin.setPasswordHash(CypherUtil.getMd5(password));
        admin.setRole(UserDTO.Role.ADMIN);
        userRepository.save(admin);

        user.setId("test-user-id");
        user.setLogin(userLogin);
        user.setPasswordHash(CypherUtil.getMd5(password));
        user.setRole(UserDTO.Role.USER);
        userRepository.save(user);
    }

    private void startServer() {
        final JAXRSServerFactoryBean jaxrsServerFactoryBean = new JAXRSServerFactoryBean();
        jaxrsServerFactoryBean.setResourceClasses(UserRestService.class);
        jaxrsServerFactoryBean.setResourceProvider(new SingletonResourceProvider(userRestService));
        jaxrsServerFactoryBean.setAddress(address);
        server = jaxrsServerFactoryBean.create();
    }

    @After
    public void destroy() {
        stopServer();
        clearData();
    }

    private void clearData() {
        userRepository.deleteAll();
    }

    private void stopServer() {
        if (server == null) return;
        server.stop();
        server.destroy();
    }

    @Test
    public void loginTest() {
        assertNotNull(UserRestServiceClient.client().login(adminLogin, password));
    }

    @Test
    public void registerTest() {
        assertNotNull(UserRestServiceClient.client().login(adminLogin, password));
    }

}
