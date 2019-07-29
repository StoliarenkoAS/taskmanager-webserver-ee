package ru.stoliarenkoas.tm.webserver.webservice.rest.client;

import feign.Feign;
import feign.Request;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.ws.rs.*;
import java.util.List;

public interface ProjectRestServiceClient {

    static ProjectRestServiceClient client() {
        final String baseUrl = "http://localhost:8080/webservice/rs/project/";
        return Feign.builder()
                .contract(new JAXRSContract())
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .options(new Request.Options(50, 50))
                .target(ProjectRestServiceClient.class, baseUrl);
    }

    @GET
    @Path("/list")
    List<ProjectDTO> getAllProjects(@HeaderParam("token") @Nullable String token);

    @GET
    @Path("/{id}")
    ProjectDTO getOneProject(@HeaderParam("token") @Nullable String token,
                             @PathParam("id") @Nullable String requestedProjectId);

    @POST
    @Path("/delete/{id}")
    void deleteOneProject(@HeaderParam("token") @Nullable String token,
                          @PathParam("id") @Nullable String requestedProjectId);

    @POST
    @Path("/clear")
    void deleteOneProject(@HeaderParam("token") @Nullable String token);

}
