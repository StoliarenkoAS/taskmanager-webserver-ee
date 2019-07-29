package ru.stoliarenkoas.tm.webserver.webservice.soap.resource;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import ru.stoliarenkoas.tm.webserver.api.websevice.soap.TaskEndpoint;
import ru.stoliarenkoas.tm.webserver.webservice.soap.TaskEndpointImpl;

public class TaskSoapResourceProvider {

    public static final String ADDRESS = "http://localhost:8080/webservice/TaskService";

    private TaskEndpointImpl taskEndpoint;
    @Autowired
    public void setTaskEndpoint(TaskEndpointImpl taskEndpoint) {
        this.taskEndpoint = taskEndpoint;
    }

    @Bean @Scope("application")
    public Server taskEndpointServer() {

        final JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();
        jaxWsServerFactoryBean.setServiceBean(taskEndpoint);
        jaxWsServerFactoryBean.setAddress(ADDRESS);
        return jaxWsServerFactoryBean.create();
    }

    @Bean @Scope("application")
    public TaskEndpoint taskEndpointClient() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(TaskEndpoint.class);
        factoryBean.setAddress(ADDRESS);
        return (TaskEndpoint) factoryBean.create();
    }

}
