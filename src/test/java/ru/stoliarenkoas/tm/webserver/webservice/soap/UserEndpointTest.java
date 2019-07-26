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
import ru.stoliarenkoas.tm.webserver.api.websevice.soap.UserEndpoint;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader.class,
        classes = {
                ru.stoliarenkoas.tm.webserver.webservice.soap.UserEndpointImpl.class,
                ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl.class,
                ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider.class})
public class UserEndpointTest {

    private final String address = "http://localhost:8080/webservice/UserService";
    private UserEndpoint client;

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
        try {
            Thread.sleep(3600 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        try {
//            URL wsdlURL = new URL(address + "?wsdl");
//            QName SERVICE_NAME = new QName("http://soap.websevice.api.webserver.tm.stoliarenkoas.ru/", "UserEndpointImplService");
//
//            //by client
////            UserEndpointImplService impl = new UserEndpointImplService(wsdlURL, SERVICE_NAME);
////            client = impl.getUserWebServiceImplPort();
//
//            //by service
//            Service service = Service.create(wsdlURL, SERVICE_NAME);
//            client = service.getPort(UserEndpoint.class);
//
////            Service service = Service.create(wsdlURL, new QName("UserServiceImplService"));
////            Dispatch<Source> disp = service.createDispatch(new QName("UserServiceImplPort"), Source.class, Service.Mode.PAYLOAD);
////
////            Source request = new StreamSource("<test/>");
////            Source response = disp.invoke(request);
////            System.out.println(response.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

//        System.out.println(response.toString());
    }

}
