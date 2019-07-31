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
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import java.util.Collections;
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
    public List<ProjectDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        final UserDTO project = authorizationController.getLoggedUser();
        if (project == null) return Collections.emptyList();
        final PageRequest pageRequest = PageRequest.of(first, pageSize);
        final List<ProjectDTO> projectList;
        try {
            final Page<ProjectDTO> projectPage = projectService.findAllByUserId(project.getId(), pageRequest);
            this.setRowCount((int)projectPage.getTotalElements());
            projectList = projectPage.getContent();
        } catch (AccessForbiddenException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return projectList;
    }

    @Override
    public Object getRowKey(ProjectDTO project) {
        return project.getId();
    }
    
}
