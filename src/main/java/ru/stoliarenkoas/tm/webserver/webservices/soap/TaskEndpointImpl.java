package ru.stoliarenkoas.tm.webserver.webservices.soap;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;
import ru.stoliarenkoas.tm.webserver.service.TaskServicePageableImpl;
import ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "ru.stoliarenkoas.tm.webserver.api.websevice.soap.TaskEndpoint")
public class TaskEndpointImpl implements ru.stoliarenkoas.tm.webserver.api.websevice.soap.TaskEndpoint {

    private TaskServicePageableImpl taskService;
    @Autowired
    public void setTaskService(TaskServicePageableImpl taskService) {
        this.taskService = taskService;
    }

    private JwtTokenProvider tokenProvider;
    @Autowired
    public void setTokenProvider(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    @WebMethod
    public List<TaskDTO> getAllTasks(@WebParam @Nullable final String token)
                                     throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return taskService.findAll(userId);
    }
    
    @Override
    @WebMethod
    public TaskDTO getOneTask(@WebParam @Nullable final String token,
                              @WebParam @Nullable final String requestedTaskId)
                              throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return taskService.findOne(userId, requestedTaskId);
    }

    @Override
    @WebMethod
    public void persistTask(@WebParam @Nullable final String token,
                            @WebParam @Nullable final TaskDTO newTask)
                            throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        taskService.persist(userId, newTask);
    }

    @Override
    @WebMethod
    public void mergeTask(@WebParam @Nullable final String token,
                          @WebParam @Nullable final TaskDTO updatedTask)
                          throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        taskService.merge(userId, updatedTask);
    }


    @Override
    @WebMethod
    public void deleteOneTask(@WebParam @Nullable final String token,
                              @WebParam @Nullable final String requestedTaskId)
                              throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        taskService.remove(userId, requestedTaskId);
    }

}
