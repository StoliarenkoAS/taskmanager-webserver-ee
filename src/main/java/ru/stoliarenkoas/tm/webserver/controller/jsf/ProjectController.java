package ru.stoliarenkoas.tm.webserver.controller.jsf;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectServicePageable;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@Controller
@SessionScope
public class ProjectController implements Serializable {

    @Nullable
    private ProjectDTO editableProject;

    @Nullable
    private LazyDataModel<ProjectDTO> projectList;
    @Autowired
    public void setProjectList(@Nullable LazyDataModel<ProjectDTO> projectList) {
        this.projectList = projectList;
    }

    @NotNull
    private AuthorizationController authorizationController;
    @Autowired
    public void setAuthorizationController(AuthorizationController authorizationController) {
        this.authorizationController = authorizationController;
    }

    @NotNull
    private ProjectServicePageable projectService;
    @Autowired
    public void setProjectService(ProjectServicePageable projectService) {
        this.projectService = projectService;
    }

    public LazyDataModel<ProjectDTO> getProjectList() {
        return projectList;
    }

    @Nullable
    public ProjectDTO getEditableProject() {
        return editableProject;
    }

    public String projectEdit(@Nullable final ProjectDTO projectDTO) {
        if (projectDTO == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error", "no project selected"));
            return null;
        }
        editableProject = projectDTO;
        return "project-edit";
    }

    public String projectCreate() {
        try {
            final UserDTO loggedUser = authorizationController.getLoggedUser();
            if (loggedUser == null) throw new AccessForbiddenException("not logged in");
            editableProject = new ProjectDTO();
            editableProject.setUserId(loggedUser.getId());
            editableProject.setName("New project");
            editableProject.setDescription("No description provided yet");
            projectService.persist(loggedUser.getId(), editableProject);
        } catch (AccessForbiddenException | IncorrectDataException e) {
            e.printStackTrace();
            final FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error", e.getMessage()));
            return "index";
        }
        return "project-edit";
    }

    public String projectSave() {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (editableProject == null) {
            context.addMessage(null, new FacesMessage("Error", "no project selected"));
            return null;
        }
        final UserDTO loggedUser = authorizationController.getLoggedUser();
        if (loggedUser == null) {
            context.addMessage(null, new FacesMessage("Error", "not logged in"));
            editableProject = null;
            return "index";
        }
        try {
            projectService.merge(loggedUser.getId(), editableProject);
            return "project-list";
        } catch (AccessForbiddenException | IncorrectDataException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Error", "access forbidden"));
            return "index";
        } finally {
            editableProject = null;
        }
    }

    public void projectRemove(@Nullable final ProjectDTO projectDTO) {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (projectDTO == null) {
            context.addMessage(null, new FacesMessage("Error", "no project selected"));
            return;
        }
        try {
            final UserDTO loggedUser = authorizationController.getLoggedUser();
            if (loggedUser == null) throw new AccessForbiddenException("not logged in");
            projectService.remove(loggedUser.getId(), projectDTO.getId());
        } catch (AccessForbiddenException | IncorrectDataException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Error", "access forbidden"));
            return;
        }
        context.addMessage(null, new FacesMessage("Success", "project removed"));
    }

}
