package ru.stoliarenkoas.tm.webserver.api.websevice.soap;

import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(name = "TaskSoapService")
public interface TaskEndpoint {

    @WebMethod
    List<TaskDTO> getAllTasks(@WebParam @Nullable String token)
            throws AccessForbiddenException;

    @WebMethod
    TaskDTO getOneTask(
            @WebParam @Nullable String token,
            @WebParam @Nullable String requestedTaskId
    ) throws AccessForbiddenException, IncorrectDataException;

    @WebMethod
    void persistTask(
            @WebParam @Nullable String token,
            @WebParam @Nullable TaskDTO newTask
    ) throws AccessForbiddenException, IncorrectDataException;

    @WebMethod
    void mergeTask(
            @WebParam @Nullable String token,
            @WebParam @Nullable TaskDTO updatedTask
    ) throws AccessForbiddenException, IncorrectDataException;

    @WebMethod
    void deleteOneTask(
            @WebParam @Nullable String token,
            @WebParam @Nullable String requestedTaskId
    ) throws AccessForbiddenException, IncorrectDataException;

}
