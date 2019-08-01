package ru.stoliarenkoas.tm.webserver.controller.model;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.controller.AuthorizationController;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

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
    public List<UserDTO> load(
            final int first,
            final int pageSize,
            final String sortField,
            final SortOrder sortOrder,
            final Map<String, Object> filters
    ) {
        final UserDTO loggedUser = authorizationController.getLoggedUser();
        final PageRequest pageRequest = PageRequest.of(first, pageSize);
        final List<UserDTO> userList;
        final Page<UserDTO> userPage = userService.findAll(loggedUser.getId(), pageRequest);
        this.setRowCount((int)userPage.getTotalElements());
        userList = userPage.getContent();
        return userList;
    }

    @Override
    public Object getRowKey(UserDTO user) {
        return user.getId();
    }

}
