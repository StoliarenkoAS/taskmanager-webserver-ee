package ru.stoliarenkoas.tm.webserver.webservice.soap.resource;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.api.websevice.soap.ProjectEndpoint;
import ru.stoliarenkoas.tm.webserver.webservice.soap.ProjectEndpointImpl;

public class ProjectSoapResourceProvider {

    public static final String ADDRESS = "http://localhost:8080/webservice/ProjectService";

    private ProjectEndpointImpl projectEndpoint;
    @Autowired
    public void setProjectEndpoint(ProjectEndpointImpl projectEndpoint) {
        this.projectEndpoint = projectEndpoint;
    }

    private UserServicePageable userService;
    @Autowired
    public void setUserService(UserServicePageable userService) {
        this.userService = userService;
    }

    @Bean @Scope("application")
    public Server projectEndpointServer() {

        final JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();
        jaxWsServerFactoryBean.setServiceBean(projectEndpoint);
        jaxWsServerFactoryBean.setAddress(ADDRESS);
        return jaxWsServerFactoryBean.create();
    }

    @Bean @Scope("application")
    public ProjectEndpoint projectEndpointClient() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(ProjectEndpoint.class);
        factoryBean.setAddress(ADDRESS);
        return (ProjectEndpoint) factoryBean.create();
    }

}
