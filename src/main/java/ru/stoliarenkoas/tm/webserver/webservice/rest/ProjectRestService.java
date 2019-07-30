package ru.stoliarenkoas.tm.webserver.webservice.rest;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.service.ProjectServicePageableImpl;
import ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

public class ProjectRestService {

    private ProjectServicePageableImpl projectService;
    @Autowired
    public void setProjectService(ProjectServicePageableImpl projectService) {
        this.projectService = projectService;
    }

    private JwtTokenProvider tokenProvider;
    @Autowired
    public void setTokenProvider(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectDTO> getAllProjects(@HeaderParam("token") @Nullable String token)
                                           throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return projectService.findAllByUserId(userId);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectDTO getOneProject(@HeaderParam("token") @Nullable String token,
                                    @PathParam("id") @Nullable String requestedProjectId)
                                    throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return projectService.findOne(userId, requestedProjectId);
    }

    @POST
    @Path("/delete/{id}")
    public void deleteOneProject(@HeaderParam("token") @Nullable String token,
                                 @PathParam("id") @Nullable String requestedProjectId)
                                 throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        projectService.remove(userId, requestedProjectId);
    }

    @POST
    @Path("/clear")
    public void deleteAllProjects(@HeaderParam("token") @Nullable String token)
                                 throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        projectService.removeAllByUserId(userId);
    }

    @POST
    @Path("/persist")
    @Consumes(MediaType.APPLICATION_JSON)
    public void persistProject(@HeaderParam("token") @Nullable String token,
                               @Nullable ProjectDTO project)
                               throws AccessForbiddenException, IOException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        projectService.persist(userId, project);
    }
    
}
