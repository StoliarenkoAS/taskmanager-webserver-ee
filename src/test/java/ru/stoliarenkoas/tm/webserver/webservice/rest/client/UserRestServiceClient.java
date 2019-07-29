package ru.stoliarenkoas.tm.webserver.webservice.rest.client;

import feign.Feign;
import feign.Request;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.ws.rs.*;
import java.util.List;

public interface UserRestServiceClient {

    static UserRestServiceClient client() {
        final String baseUrl = "http://localhost:8080/webservice/rs/user/";
        return Feign.builder()
                .contract(new JAXRSContract())
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .options(new Request.Options(50, 50))
                .target(UserRestServiceClient.class, baseUrl);
    }

    @POST
    @Path("/register")
    @Consumes("application/json")
    void userRegister(@QueryParam("login") @Nullable String login,
                      @QueryParam("password") @Nullable String password);

    @POST
    @Path("/login")
    String login(@QueryParam("login") String login, @QueryParam("password") String password);

    @GET
    @Path("/list")
    List<UserDTO> getAllUsers(@HeaderParam("token") @Nullable String token);

    @GET
    @Path("/{id}")
    UserDTO getOneUser(@HeaderParam("token") @Nullable String token,
                       @PathParam("id") @Nullable String requestedUserId);

    @POST
    @Path("/delete/{id}")
    void deleteOneUser(@HeaderParam("token") @Nullable String token,
                       @PathParam("id") @Nullable String requestedUserId);

}
