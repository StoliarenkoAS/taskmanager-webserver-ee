package ru.stoliarenkoas.tm.webserver.webservice.rest.client;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

public interface TaskRestServiceClient {

    static TaskRestServiceClient client() {
        final String baseUrl = "http://localhost:8080/webservice/rs/task/";
        return Feign.builder()
                .contract(new JAXRSContract())
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .retryer(new Retryer.Default(50, 50, 1))
                .options(new Request.Options(50, 500))
                .target(TaskRestServiceClient.class, baseUrl);
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    List<TaskDTO> getAllTasks(@HeaderParam("token") @Nullable String token);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TaskDTO getOneTask(
            @HeaderParam("token") @Nullable String token,
            @PathParam("id") @Nullable String requestedTaskId
    );

    @POST
    @Path("/delete/{id}")
    void deleteOneTask(
            @HeaderParam("token") @Nullable String token,
            @PathParam("id") @Nullable String requestedTaskId
    );

    @POST
    @Path("/clear/{projectId}")
    void deleteProjectTasks(
            @HeaderParam("token") @Nullable String token,
            @PathParam("projectId") @Nullable String projectId
    );

    @POST
    @Path("/persist")
    @Consumes(MediaType.APPLICATION_JSON)
    void persistTask(
            @HeaderParam("token") @Nullable String token,
            @Nullable TaskDTO task
    );

}
