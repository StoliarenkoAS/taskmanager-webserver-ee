package ru.stoliarenkoas.tm.webserver.controller.model;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.controller.jsf.AuthorizationController;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@SessionScope
public class UserDataModel extends LazyDataModel<UserDTO> {

    private UserServicePageable userService;
    @Autowired
    public void setUserService(UserServicePageable userService) {
        this.userService = userService;
    }

    private AuthorizationController authorizationController;
    @Autowired
    public void setAuthorizationController(AuthorizationController authorizationController) {
        this.authorizationController = authorizationController;
    }

    @Override
    public List<UserDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        final UserDTO user = authorizationController.getLoggedUser();
        if (user == null) return Collections.emptyList();
        final PageRequest pageRequest = PageRequest.of(first, pageSize);
        final List<UserDTO> userList;
        try {
            final Page<UserDTO> userPage = userService.findAll(user.getId(), pageRequest);
            this.setRowCount((int)userPage.getTotalElements());
            userList = userPage.getContent();
        } catch (AccessForbiddenException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return userList;
    }

    @Override
    public Object getRowKey(UserDTO user) {
        return user.getId();
    }

}
