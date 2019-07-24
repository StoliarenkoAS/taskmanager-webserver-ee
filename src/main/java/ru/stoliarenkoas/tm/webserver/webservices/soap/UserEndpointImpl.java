package ru.stoliarenkoas.tm.webserver.webservices.soap;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl;
import ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "ru.stoliarenkoas.tm.webserver.api.websevice.soap.UserEndpoint")
public class UserEndpointImpl implements ru.stoliarenkoas.tm.webserver.api.websevice.soap.UserEndpoint {

    private UserServicePageableImpl userService;
    @Autowired
    public void setUserService(UserServicePageableImpl userService) {
        this.userService = userService;
    }

    private JwtTokenProvider tokenProvider;
    @Autowired
    public void setTokenProvider(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    @WebMethod
    public void userRegister(@WebParam @Nullable final String login,
                             @WebParam @Nullable final String password)
                             throws IncorrectDataException {
        userService.register(login, password);
    }

    @Override
    @WebMethod
    public String userLogin(@WebParam @Nullable final String login,
                            @WebParam @Nullable final String password)
                            throws IncorrectDataException {
        final UserDTO loggedUser = userService.login(login, password);
        return tokenProvider.createToken(loggedUser.getId(), loggedUser.getRole().toString());
    }

    @Override
    @WebMethod
    public List<UserDTO> getAllUsers(@WebParam @Nullable final String token)
                                     throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return userService.findAll(userId);
    }

    @Override
    @WebMethod
    public UserDTO getOneUser(@WebParam @Nullable final String token,
                              @WebParam @Nullable final String requestedUserId)
                              throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return userService.findOne(userId, requestedUserId);
    }

    @Override
    @WebMethod
    public void deleteOneUser(@WebParam @Nullable final String token,
                              @WebParam @Nullable final String requestedUserId)
                              throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        userService.remove(userId, requestedUserId);
    }

}
