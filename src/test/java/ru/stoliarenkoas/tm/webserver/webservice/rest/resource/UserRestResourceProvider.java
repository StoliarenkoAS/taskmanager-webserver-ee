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
import ru.stoliarenkoas.tm.webserver.webservice.rest.UserRestService;
import ru.stoliarenkoas.tm.webserver.webservice.rest.client.UserRestServiceClient;

public class UserRestResourceProvider {

    private final static String ADDRESS = "http://localhost:8080/webservice/rs/user";

    private UserRestService userRestService;
    @Autowired
    public void setUserRestService(UserRestService userRestService) {
        this.userRestService = userRestService;
    }

    @Bean @Scope("singleton")
    public Server userRestServer() {
        final JAXRSServerFactoryBean jaxrsServerFactoryBean = new JAXRSServerFactoryBean();
        jaxrsServerFactoryBean.setResourceClasses(UserRestService.class);
        jaxrsServerFactoryBean.setProvider(new JacksonJaxbJsonProvider());
        jaxrsServerFactoryBean.setResourceProvider(new SingletonResourceProvider(userRestService));
        jaxrsServerFactoryBean.setAddress(ADDRESS);
        return jaxrsServerFactoryBean.create();
    }

    @Bean @Scope("singleton")
    public UserRestServiceClient userRestClient() {
        return UserRestServiceClient.client();
    }

    @Bean @Lazy
    @Scope("singleton")
    public String userToken() throws IncorrectDataException {
        return userRestService.userLogin(DataConstants.USER_LOGIN, DataConstants.PASSWORD);
    }

    @Bean @Lazy
    @Scope("singleton")
    public String adminToken() throws IncorrectDataException {
        return userRestService.userLogin(DataConstants.ADMIN_LOGIN, DataConstants.PASSWORD);
    }

}
