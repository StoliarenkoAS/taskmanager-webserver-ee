package ru.stoliarenkoas.tm.webserver.configuration;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.stoliarenkoas.tm.webserver.webservices.soap.ProjectEndpointImpl;
import ru.stoliarenkoas.tm.webserver.webservices.soap.TaskEndpointImpl;
import ru.stoliarenkoas.tm.webserver.webservices.soap.UserEndpointImpl;

import javax.xml.ws.Endpoint;

//@EnableWebMvc
//@Configuration
public class WebserviceConfiguration {
//
//    @Bean(name = Bus.DEFAULT_BUS_ID)
//    public SpringBus springBus() {
//        return new SpringBus();
//    }
//
//    @Bean
//    public UserEndpointImpl userEndpointImpl() {
//        return new UserEndpointImpl();
//    }
//
//    @Bean
//    public Endpoint userEndpoint() {
//        final EndpointImpl endpoint = new EndpointImpl(springBus(), userEndpointImpl());
//        endpoint.publish("/UserService");
//        return endpoint;
//    }
//
//    @Bean
//    public ProjectEndpointImpl projectEndpointImpl() {
//        return new ProjectEndpointImpl();
//    }
//
//    @Bean
//    public Endpoint projectEndpoint() {
//        final EndpointImpl endpoint = new EndpointImpl(springBus(), projectEndpointImpl());
//        endpoint.publish("/ProjectService");
//        return endpoint;
//    }
//
//    @Bean
//    public TaskEndpointImpl taskEndpointImpl() {
//        return new TaskEndpointImpl();
//    }
//
//    @Bean
//    public Endpoint taskEndpoint() {
//        final EndpointImpl endpoint = new EndpointImpl(springBus(), taskEndpointImpl());
//        endpoint.publish("/TaskService");
//        return endpoint;
//    }

}