package ru.stoliarenkoas.tm.webserver.webservice.rest.client;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

public interface ProjectRestServiceClient {

    static ProjectRestServiceClient client() {
        final String baseUrl = "http://localhost:8080/webservice/rs/project/";
        return Feign.builder()
                .contract(new JAXRSContract())
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .retryer(new Retryer.Default(50, 50, 1))
                .options(new Request.Options(50, 500))
                .target(ProjectRestServiceClient.class, baseUrl);
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    List<ProjectDTO> getAllProjects(@HeaderParam("token") @Nullable String token);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    ProjectDTO getOneProject(@HeaderParam("token") @Nullable String token,
                             @PathParam("id") @Nullable String requestedProjectId);

    @POST
    @Path("/delete/{id}")
    void deleteOneProject(@HeaderParam("token") @Nullable String token,
                          @PathParam("id") @Nullable String requestedProjectId);

    @POST
    @Path("/clear")
    void deleteAllProjects(@HeaderParam("token") @Nullable String token);

    @POST
    @Path("/persist")
    @Consumes(MediaType.APPLICATION_JSON)
    void persistProject(@HeaderParam("token") @Nullable String token,
                        ProjectDTO project)
                        throws AccessForbiddenException, IOException, IncorrectDataException;

}
