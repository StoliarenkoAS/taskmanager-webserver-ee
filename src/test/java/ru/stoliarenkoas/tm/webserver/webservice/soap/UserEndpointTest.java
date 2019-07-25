package ru.stoliarenkoas.tm.webserver.webservice.soap;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader.class,
        classes = {
                ru.stoliarenkoas.tm.webserver.webservice.soap.UserEndpointImpl.class,
                ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider.class})
public class UserEndpointTest {

    private final String address = "http://localhost:8080/webservice/ws/UserService";

    private UserEndpointImpl userEndpoint;
    @Autowired
    public void setUserEndpoint(UserEndpointImpl userEndpoint) {
        this.userEndpoint = userEndpoint;
    }

    @Before
    public void init() {
        final JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();
        jaxWsServerFactoryBean.setServiceBean(userEndpoint);
        jaxWsServerFactoryBean.setAddress(address);
        jaxWsServerFactoryBean.create();

    }

    @Test
    public void test() {
    }

}
