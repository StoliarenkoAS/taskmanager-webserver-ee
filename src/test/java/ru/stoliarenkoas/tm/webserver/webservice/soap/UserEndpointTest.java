package ru.stoliarenkoas.tm.webserver.webservice.soap;

import org.apache.cxf.endpoint.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.stoliarenkoas.tm.webserver.api.websevice.soap.UserEndpoint;
import ru.stoliarenkoas.tm.webserver.webservice.soap.resource.UserSoapServerProvider;

import static org.junit.Assert.assertNotNull;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader.class,
        classes = {
                UserEndpointImpl.class,
                UserSoapServerProvider.class,
                ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider.class})
public class UserEndpointTest {

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

    @Before
    public void init() {
        System.out.println("init");
    }

    @After
    public void destroy() {
        System.out.println("destroy");
    }

    @Test
    public void test1() {
        assertNotNull(userEndpointClient.test());
    }

    @Test
    public void test2() {
        assertNotNull(userEndpointClient.test());
    }

}
