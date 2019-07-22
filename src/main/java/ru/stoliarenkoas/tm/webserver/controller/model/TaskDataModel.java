package ru.stoliarenkoas.tm.webserver.controller.model;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.stoliarenkoas.tm.webserver.api.service.TaskServicePageable;
import ru.stoliarenkoas.tm.webserver.controller.jsf.AuthorizationController;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import java.util.Collections;
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
    public List<TaskDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        final UserDTO task = authorizationController.getLoggedUser();
        if (task == null) return Collections.emptyList();
        final PageRequest pageRequest = PageRequest.of(first, pageSize);
        final List<TaskDTO> taskList;
        try {
            final Page<TaskDTO> taskPage = taskService.findAllByUserId(task.getId(), pageRequest);
            this.setRowCount((int)taskPage.getTotalElements());
            taskList = taskPage.getContent();
        } catch (AccessForbiddenException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return taskList;
    }

    @Override
    public Object getRowKey(TaskDTO task) {
        return task.getId();
    }
    
}
