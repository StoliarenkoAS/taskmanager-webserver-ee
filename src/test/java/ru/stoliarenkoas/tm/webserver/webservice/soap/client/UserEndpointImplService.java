package ru.stoliarenkoas.tm.webserver.webservice.soap.client;

import ru.stoliarenkoas.tm.webserver.api.websevice.soap.UserEndpoint;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;
import java.net.URL;


@WebServiceClient(
        name = "UserEndpointImplService",
        targetNamespace = "http://soap.websevice.api.webserver.tm.stoliarenkoas.ru/",
        wsdlLocation = "http://localhost:8080/webservice/UserService?wsdl"
)
public class UserEndpointImplService extends Service {

    public UserEndpointImplService(URL wsdlDocumentLocation, QName serviceName) {
        super(wsdlDocumentLocation, serviceName);
    }

    public UserEndpoint getUserWebServiceImplPort() {
        return (UserEndpoint) super.getPort(
                new QName("http://soap.websevice.api.webserver.tm.stoliarenkoas.ru/", "UserEndpointImplService"),
                UserEndpoint.class);
    }

}
