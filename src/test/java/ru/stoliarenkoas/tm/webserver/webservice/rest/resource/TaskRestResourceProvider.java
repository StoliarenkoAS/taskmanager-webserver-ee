package ru.stoliarenkoas.tm.webserver.webservice.rest.resource;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import ru.stoliarenkoas.tm.webserver.webservice.rest.TaskRestService;
import ru.stoliarenkoas.tm.webserver.webservice.rest.client.TaskRestServiceClient;

public class TaskRestResourceProvider {

    private final static String ADDRESS = "http://localhost:8080/webservice/rs/task";

    private TaskRestService taskRestService;
    @Autowired
    public void setTaskRestService(TaskRestService taskRestService) {
        this.taskRestService = taskRestService;
    }

    @Bean @Scope("singleton")
    public Server taskRestServer() {
        final JAXRSServerFactoryBean jaxrsServerFactoryBean = new JAXRSServerFactoryBean();
        jaxrsServerFactoryBean.setResourceClasses(TaskRestService.class);
        jaxrsServerFactoryBean.setProvider(new JacksonJaxbJsonProvider());
        jaxrsServerFactoryBean.setResourceProvider(new SingletonResourceProvider(taskRestService));
        jaxrsServerFactoryBean.setAddress(ADDRESS);
        return jaxrsServerFactoryBean.create();
    }

    @Bean @Scope("singleton")
    public TaskRestServiceClient taskRestClient() {
        return TaskRestServiceClient.client();
    }

}
