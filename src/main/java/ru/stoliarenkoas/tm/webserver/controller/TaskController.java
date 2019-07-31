package ru.stoliarenkoas.tm.webserver.controller;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectServicePageable;
import ru.stoliarenkoas.tm.webserver.api.service.TaskServicePageable;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@Controller
@SessionScope
public class TaskController implements Serializable {

    @Nullable
    private TaskDTO editableTask;

    @Nullable
    private LazyDataModel<TaskDTO> taskList;
    @Autowired
    public void setTaskList(@Nullable LazyDataModel<TaskDTO> taskList) {
        this.taskList = taskList;
    }

    @Nullable
    private List<ProjectDTO> projects;

    @NotNull
    private AuthorizationController authorizationController;
    @Autowired
    public void setAuthorizationController(AuthorizationController authorizationController) {
        this.authorizationController = authorizationController;
    }

    @NotNull
    private TaskServicePageable taskService;
    @Autowired
    public void setTaskService(TaskServicePageable taskService) {
        this.taskService = taskService;
    }

    @NotNull
    private ProjectServicePageable projectService;
    @Autowired
    public void setProjectService(ProjectServicePageable projectService) {
        this.projectService = projectService;
    }

    public LazyDataModel<TaskDTO> getTaskList() {
        return taskList;
    }

    public List<ProjectDTO> getProjects() {
        return projects;
    }

    @Nullable
    public TaskDTO getEditableTask() {
        return editableTask;
    }

    public String taskEdit(@Nullable final TaskDTO taskDTO) {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (taskDTO == null) {
            context.addMessage(null, new FacesMessage("Error", "no task selected"));
            return null;
        }
        try {
            loadProjects();
        } catch (AccessForbiddenException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Error", e.getMessage()));
            return "task-list";
        }
        editableTask = taskDTO;
        return "task-edit";
    }

    public String taskCreate() {
        try {
            final UserDTO loggedUser = authorizationController.getLoggedUser();
            if (loggedUser == null) throw new AccessForbiddenException("not logged in");
            loadProjects();
            editableTask = new TaskDTO();
            editableTask.setUserId(loggedUser.getId());
            editableTask.setName("New task");
            editableTask.setDescription("No description provided yet");
        } catch (AccessForbiddenException e) {
            e.printStackTrace();
            final FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error", e.getMessage()));
            return "index";
        }
        return "task-edit";
    }

    public String taskSave() {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (editableTask == null) {
            context.addMessage(null, new FacesMessage("Error", "no task selected"));
            return null;
        }
        final UserDTO loggedUser = authorizationController.getLoggedUser();
        if (loggedUser == null) {
            context.addMessage(null, new FacesMessage("Error", "not logged in"));
            editableTask = null;
            return "index";
        }
        try {
            taskService.merge(loggedUser.getId(), editableTask);
            return "task-list";
        } catch (AccessForbiddenException | IncorrectDataException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Error", "access forbidden"));
            return "index";
        } finally {
            editableTask = null;
        }
    }

    public void taskRemove(@Nullable final TaskDTO taskDTO) {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (taskDTO == null) {
            context.addMessage(null, new FacesMessage("Error", "no task selected"));
            return;
        }
        try {
            final UserDTO loggedUser = authorizationController.getLoggedUser();
            if (loggedUser == null) throw new AccessForbiddenException("not logged in");
            taskService.remove(loggedUser.getId(), taskDTO.getId());
        } catch (AccessForbiddenException | IncorrectDataException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Error", "access forbidden"));
            return;
        }
        context.addMessage(null, new FacesMessage("Success", "task removed"));
    }

    private void loadProjects() throws AccessForbiddenException {
        final UserDTO loggedUser = authorizationController.getLoggedUser();
        if (loggedUser == null) throw new AccessForbiddenException("not logged in");
        projects = projectService.findAllByUserId(loggedUser.getId());
    }

}
