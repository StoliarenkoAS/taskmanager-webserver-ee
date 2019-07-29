package ru.stoliarenkoas.tm.webserver.webservice.soap.resource;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import ru.stoliarenkoas.tm.webserver.api.websevice.soap.UserEndpoint;
import ru.stoliarenkoas.tm.webserver.webservice.soap.UserEndpointImpl;

public class UserSoapServerProvider {
    public static final String ADDRESS = "http://localhost:8080/webservice/UserService";

    private UserEndpointImpl userEndpoint;
    @Autowired
    public void setUserEndpoint(UserEndpointImpl userEndpoint) {
        this.userEndpoint = userEndpoint;
    }

    @Bean @Scope("application")
    public Server userEndpointServer() {

        final JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();
        jaxWsServerFactoryBean.setServiceBean(userEndpoint);
        jaxWsServerFactoryBean.setAddress(ADDRESS);
        return jaxWsServerFactoryBean.create();
    }

    @Bean @Scope("application")
    public UserEndpoint userEndpointClient() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(UserEndpoint.class);
        factoryBean.setAddress(ADDRESS);
        return (UserEndpoint) factoryBean.create();
    }

}
