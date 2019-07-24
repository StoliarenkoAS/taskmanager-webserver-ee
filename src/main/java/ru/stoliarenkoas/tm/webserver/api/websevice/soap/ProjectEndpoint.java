package ru.stoliarenkoas.tm.webserver.api.websevice.soap;

import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ProjectEndpoint {

    @WebMethod
    List<ProjectDTO> getAllProjects(@WebParam @Nullable String token)
                                    throws AccessForbiddenException;

    @WebMethod
    ProjectDTO getOneProject(@WebParam @Nullable String token,
                             @WebParam @Nullable String requestedProjectId)
                             throws AccessForbiddenException, IncorrectDataException;

    @WebMethod
    void persistProject(@WebParam @Nullable String token,
                        @WebParam @Nullable ProjectDTO newProject)
                        throws AccessForbiddenException, IncorrectDataException;

    @WebMethod
    void mergeProject(@WebParam @Nullable String token,
                      @WebParam @Nullable ProjectDTO updatedProject)
                      throws AccessForbiddenException, IncorrectDataException;

    @WebMethod
    void deleteOneProject(@WebParam @Nullable String token,
                          @WebParam @Nullable String requestedProjectId)
                          throws AccessForbiddenException, IncorrectDataException;

}
