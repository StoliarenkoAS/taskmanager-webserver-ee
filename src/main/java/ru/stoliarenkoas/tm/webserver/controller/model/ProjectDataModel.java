package ru.stoliarenkoas.tm.webserver.controller.model;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectServicePageable;
import ru.stoliarenkoas.tm.webserver.controller.AuthorizationController;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import java.util.List;
import java.util.Map;

@Component
@SessionScope
public class ProjectDataModel extends LazyDataModel<ProjectDTO> {

    private ProjectServicePageable projectService;
    @Autowired
    public void setProjectService(ProjectServicePageable projectService) {
        this.projectService = projectService;
    }

    private AuthorizationController authorizationController;
    @Autowired
    public void setAuthorizationController(AuthorizationController authorizationController) {
        this.authorizationController = authorizationController;
    }

    @Override
    public List<ProjectDTO> load(
            final int first,
            final int pageSize,
            final String sortField,
            final SortOrder sortOrder,
            final Map<String, Object> filters
    ) {
        final UserDTO loggedUser = authorizationController.getLoggedUser();
        final PageRequest pageRequest = PageRequest.of(first, pageSize);
        final List<ProjectDTO> projectList;
        final Page<ProjectDTO> projectPage = projectService.findAllByUserId(loggedUser.getId(), pageRequest);
        this.setRowCount((int)projectPage.getTotalElements());
        projectList = projectPage.getContent();
        return projectList;
    }

    @Override
    public Object getRowKey(ProjectDTO project) {
        return project.getId();
    }
    
}
