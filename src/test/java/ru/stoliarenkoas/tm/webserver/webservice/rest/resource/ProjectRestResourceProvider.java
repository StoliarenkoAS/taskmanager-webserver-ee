package ru.stoliarenkoas.tm.webserver.webservice.rest.resource;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import ru.stoliarenkoas.tm.webserver.DataConstants;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.webservice.rest.ProjectRestService;
import ru.stoliarenkoas.tm.webserver.webservice.rest.client.ProjectRestServiceClient;

public class ProjectRestResourceProvider {

    private final static String ADDRESS = "http://localhost:8080/webservice/rs/project";

    private ProjectRestService projectRestService;
    @Autowired
    public void setProjectRestService(ProjectRestService projectRestService) {
        this.projectRestService = projectRestService;
    }

    @Bean @Scope("singleton")
    public Server projectRestServer() {
        final JAXRSServerFactoryBean jaxrsServerFactoryBean = new JAXRSServerFactoryBean();
        jaxrsServerFactoryBean.setResourceClasses(ProjectRestService.class);
        jaxrsServerFactoryBean.setProvider(new JacksonJaxbJsonProvider());
        jaxrsServerFactoryBean.setResourceProvider(new SingletonResourceProvider(projectRestService));
        jaxrsServerFactoryBean.setAddress(ADDRESS);
        return jaxrsServerFactoryBean.create();
    }

    @Bean @Scope("singleton")
    public ProjectRestServiceClient projectRestClient() {
        return ProjectRestServiceClient.client();
    }

}
