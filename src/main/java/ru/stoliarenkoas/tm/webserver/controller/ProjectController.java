package ru.stoliarenkoas.tm.webserver.controller;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectServicePageable;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class ProjectController extends SpringBeanAutowiringSupport implements Serializable {

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
        editableProject = projectDTO;
        return "project-edit";
    }

    public String projectCreate() {
        final UserDTO loggedUser = authorizationController.getLoggedUser();
        editableProject = new ProjectDTO();
        editableProject.setUserId(loggedUser.getId());
        editableProject.setName("New project");
        editableProject.setDescription("No description provided yet");
        projectService.persist(loggedUser.getId(), editableProject);
        return "project-edit";
    }

    public String projectSave() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final UserDTO loggedUser = authorizationController.getLoggedUser();
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
        try {
            final UserDTO loggedUser = authorizationController.getLoggedUser();
            projectService.remove(loggedUser.getId(), projectDTO.getId());
        } catch (AccessForbiddenException | IncorrectDataException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Error", "access forbidden"));
            return;
        }
        context.addMessage(null, new FacesMessage("Success", "project removed"));
    }

}
