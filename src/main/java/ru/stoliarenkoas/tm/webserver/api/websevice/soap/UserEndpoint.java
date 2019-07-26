package ru.stoliarenkoas.tm.webserver.api.websevice.soap;

import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(name = "UserSoapService")
public interface UserEndpoint {

    @WebMethod
    String test();

    @WebMethod
    void userRegister(@WebParam @Nullable String login,
                      @WebParam @Nullable String password)
                      throws IncorrectDataException;

    @WebMethod
    String userLogin(@WebParam @Nullable String login,
                     @WebParam @Nullable String password)
                     throws IncorrectDataException;

    @WebMethod
    List<UserDTO> getAllUsers(@WebParam @Nullable String token)
                              throws AccessForbiddenException;

    @WebMethod
    UserDTO getOneUser(@WebParam @Nullable String token,
                       @WebParam @Nullable String requestedUserId)
                       throws AccessForbiddenException;

    @WebMethod
    void deleteOneUser(@WebParam @Nullable String token,
                       @WebParam @Nullable String requestedUserId)
                       throws AccessForbiddenException;

}
