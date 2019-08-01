package ru.stoliarenkoas.tm.webserver.controller.model;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.stoliarenkoas.tm.webserver.api.service.TaskServicePageable;
import ru.stoliarenkoas.tm.webserver.controller.AuthorizationController;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import java.util.List;
import java.util.Map;

@Component
@SessionScope
public class TaskDataModel extends LazyDataModel<TaskDTO> {

    private TaskServicePageable taskService;
    @Autowired
    public void setTaskService(TaskServicePageable taskService) {
        this.taskService = taskService;
    }

    private AuthorizationController authorizationController;
    @Autowired
    public void setAuthorizationController(AuthorizationController authorizationController) {
        this.authorizationController = authorizationController;
    }

    @Override
    public List<TaskDTO> load(
            final int first,
            final int pageSize,
            final String sortField,
            final SortOrder sortOrder,
            final Map<String, Object> filters
    ) {
        final UserDTO loggedUser = authorizationController.getLoggedUser();
        final PageRequest pageRequest = PageRequest.of(first, pageSize);
        final List<TaskDTO> taskList;
        final Page<TaskDTO> taskPage = taskService.findAllByUserId(loggedUser.getId(), pageRequest);
        this.setRowCount((int)taskPage.getTotalElements());
        taskList = taskPage.getContent();
        return taskList;
    }

    @Override
    public Object getRowKey(TaskDTO task) {
        return task.getId();
    }
    
}
