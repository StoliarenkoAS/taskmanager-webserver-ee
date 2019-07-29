package ru.stoliarenkoas.tm.webserver.webservice.rest.client;

import feign.Feign;
import feign.Request;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;

import javax.ws.rs.*;
import java.util.List;

public interface TestRestServiceClient {

    static TestRestServiceClient client() {
        final String baseUrl = "http://localhost:8080/webservice/rs/project/";
        return Feign.builder()
                .contract(new JAXRSContract())
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .options(new Request.Options(50, 50))
                .target(TestRestServiceClient.class, baseUrl);
    }

    @GET
    @Path("/list")
    List<TaskDTO> getAllTasks(@HeaderParam("token") @Nullable String token);

    @GET
    @Path("/{id}")
    TaskDTO getOneTask(@HeaderParam("token") @Nullable String token,
                       @PathParam("id") @Nullable String requestedTaskId);

    @POST
    @Path("/delete/{id}")
    void deleteOneTask(@HeaderParam("token") @Nullable String token,
                       @PathParam("id") @Nullable String requestedTaskId);

    @POST
    @Path("/clear/{projectId}")
    void deleteProjectTasks(@HeaderParam("token") @Nullable String token,
                            @PathParam("projectId") @Nullable String projectId);

}
