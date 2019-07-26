package ru.stoliarenkoas.tm.webserver.webservice.rest;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl;
import ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider;

import javax.ws.rs.*;
import java.util.List;

public class UserRestService {

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

    @POST
    @Path("/register")
    @Consumes("application/json")
    public void userRegister(@QueryParam("login") @Nullable String login,
                             @QueryParam("password") @Nullable String password)
                             throws IncorrectDataException {
        userService.register(login, password);
    }

    @POST
    @Path("/login")
    public String userLogin(@QueryParam("login") @Nullable String login,
                            @QueryParam("password") @Nullable String password)
                            throws IncorrectDataException {
        final UserDTO userDTO = userService.login(login, password);
        return tokenProvider.createToken(userDTO.getId(), userDTO.getRole().toString());
    }

    @GET
    @Path("/list")
    public List<UserDTO> getAllUsers(@HeaderParam("token") @Nullable String token)
                                     throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return userService.findAll(userId);
    }

    @GET
    @Path("/{id}")
    public UserDTO getOneUser(@HeaderParam("token") @Nullable String token,
                              @PathParam("id") @Nullable String requestedUserId)
                              throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return userService.findOne(userId, requestedUserId);
    }

    @POST
    @Path("/delete/{id}")
    public void deleteOneUser(@HeaderParam("token") @Nullable String token,
                              @PathParam("id") @Nullable String requestedUserId)
                              throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        userService.remove(userId, requestedUserId);
    }

}
