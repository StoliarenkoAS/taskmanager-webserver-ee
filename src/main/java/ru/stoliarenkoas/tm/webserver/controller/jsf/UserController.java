package ru.stoliarenkoas.tm.webserver.controller.jsf;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@Controller
@SessionScope
public class UserController implements Serializable {

    @Nullable
    private UserDTO editableUser;

    @Nullable
    private LazyDataModel<UserDTO> userList;
    @Autowired
    public void setUserList(@Nullable LazyDataModel<UserDTO> userList) {
        this.userList = userList;
    }

    @NotNull
    private AuthorizationController authorizationController;
    @Autowired
    public void setAuthorizationController(AuthorizationController authorizationController) {
        this.authorizationController = authorizationController;
    }

    @NotNull
    private UserServicePageable userService;
    @Autowired
    public void setUserService(UserServicePageable userService) {
        this.userService = userService;
    }

    public LazyDataModel<UserDTO> getUserList() {
        return userList;
    }

    @Nullable
    public UserDTO getEditableUser() {
        return editableUser;
    }

    public String userEdit(@Nullable final UserDTO userDTO) {
        if (userDTO == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error", "no user selected"));
            return null;
        }
        editableUser = userDTO;
        return "user-edit";
    }

    public String userSave() {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (editableUser == null) {
            context.addMessage(null, new FacesMessage("Error", "no user selected"));
            return null;
        }
        final UserDTO loggedUser = authorizationController.getLoggedUser();
        if (loggedUser == null) {
            context.addMessage(null, new FacesMessage("Error", "not logged in"));
            editableUser = null;
            return "index";
        }
        try {
            userService.merge(loggedUser.getId(), editableUser);
            return "user-list";
        } catch (AccessForbiddenException | IncorrectDataException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Error", "access forbidden"));
            return "index";
        } finally {
            editableUser = null;
        }
    }

    public void userRemove(@Nullable final UserDTO userDTO) {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (userDTO == null) {
            context.addMessage(null, new FacesMessage("Error", "no user selected"));
            return;
        }
        try {
            final UserDTO loggedUser = authorizationController.getLoggedUser();
            if (loggedUser == null) throw new AccessForbiddenException("not logged in");
            userService.remove(loggedUser.getId(), userDTO.getId());
        } catch (AccessForbiddenException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Error", "access forbidden"));
            return;
        }
        context.addMessage(null, new FacesMessage("Success", "user removed"));
    }

}
