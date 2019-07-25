package ru.stoliarenkoas.tm.webserver.webservice.soap;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.service.ProjectServicePageableImpl;
import ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "ru.stoliarenkoas.tm.webserver.api.websevice.soap.ProjectEndpoint")
public class ProjectEndpointImpl implements ru.stoliarenkoas.tm.webserver.api.websevice.soap.ProjectEndpoint {

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

    @Override
    @WebMethod
    public List<ProjectDTO> getAllProjects(@WebParam @Nullable final String token)
                                           throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return projectService.findAllByUserId(userId);
    }

    @Override
    @WebMethod
    public ProjectDTO getOneProject(@WebParam @Nullable final String token,
                                    @WebParam @Nullable final String requestedProjectId)
                                    throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return projectService.findOne(userId, requestedProjectId);
    }

    @Override
    @WebMethod
    public void persistProject(@WebParam @Nullable final String token,
                               @WebParam @Nullable final ProjectDTO newProject)
                               throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        projectService.persist(userId, newProject);
    }

    @Override
    @WebMethod
    public void mergeProject(@WebParam @Nullable final String token,
                             @WebParam @Nullable final ProjectDTO updatedProject)
                             throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        projectService.merge(userId, updatedProject);
    }


    @Override
    @WebMethod
    public void deleteOneProject(@WebParam @Nullable final String token,
                                 @WebParam @Nullable final String requestedProjectId)
                                 throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        projectService.remove(userId, requestedProjectId);
    }

}