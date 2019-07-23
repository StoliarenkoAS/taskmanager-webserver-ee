package ru.stoliarenkoas.tm.webserver.webservices.soap;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.service.ProjectServicePageableImpl;
import ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService
public class ProjectEndpoint {

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

    @WebMethod
    public List<ProjectDTO> getAllProjects(@WebParam @Nullable final String token)
                                           throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return projectService.findAllByUserId(userId);
    }

    @WebMethod
    public ProjectDTO getOneProject(@WebParam @Nullable final String token,
                                    @WebParam @Nullable final String requestedProjectId)
                                    throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return projectService.findOne(userId, requestedProjectId);
    }

    @WebMethod
    public void persistProject(@WebParam @Nullable final String token,
                               @WebParam @Nullable final ProjectDTO newProject)
                               throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        projectService.persist(userId, newProject);
    }

    @WebMethod
    public void mergeProject(@WebParam @Nullable final String token,
                             @WebParam @Nullable final ProjectDTO updatedProject)
                             throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        projectService.merge(userId, updatedProject);
    }


    @WebMethod
    public void deleteOneProject(@WebParam @Nullable final String token,
                                 @WebParam @Nullable final String requestedProjectId)
                                 throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        projectService.remove(userId, requestedProjectId);
    }

}
