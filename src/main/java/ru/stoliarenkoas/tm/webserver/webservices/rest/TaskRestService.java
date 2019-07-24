package ru.stoliarenkoas.tm.webserver.webservices.rest;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;
import ru.stoliarenkoas.tm.webserver.service.TaskServicePageableImpl;
import ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider;

import javax.ws.rs.*;
import java.util.List;

public class TaskRestService {

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

    @GET
    @Path("/list")
    public List<TaskDTO> getAllTasks(@HeaderParam("token") @Nullable String token)
            throws AccessForbiddenException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return taskService.findAll(userId);
    }

    @GET
    @Path("/{id}")
    public TaskDTO getOneTask(@HeaderParam("token") @Nullable String token,
                                    @PathParam("id") @Nullable String requestedTaskId)
            throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        return taskService.findOne(userId, requestedTaskId);
    }

    @POST
    @Path("/delete/{id}")
    public void deleteOneTask(@HeaderParam("token") @Nullable String token,
                              @PathParam("id") @Nullable String requestedTaskId)
            throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        taskService.remove(userId, requestedTaskId);
    }

    @POST
    @Path("/clear/{projectId}")
    public void deleteProjectTasks(@HeaderParam("token") @Nullable String token,
                              @PathParam("projectId") @Nullable String projectId)
            throws AccessForbiddenException, IncorrectDataException {
        if (token == null) throw new AccessForbiddenException("not logged in");
        final String userId = tokenProvider.getUserId(token);
        taskService.removeByProjectId(userId, projectId);
    }
    
}
