package ru.stoliarenkoas.tm.webserver.controller;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Component
@SessionScope
public class AuthorizationController {

    @Nullable
    private UserDTO loggedUser;

    @Nullable
    private String loginInput;

    @Nullable
    private String passwordInput;

    @Nullable
    private String passwordConfirmation;

    @NotNull
    private UserServicePageable userService;
    @Autowired
    public void setUserService(@NotNull final UserServicePageable userService) {
        this.userService = userService;
    }

    public UserDTO getLoggedUser() {
        return loggedUser;
    }

    public String getUserName() {
        return loggedUser == null ? null : loggedUser.getName();
    }

    @Nullable
    public String getLoginInput() {
        return loginInput;
    }

    public void setLoginInput(@Nullable final String loginInput) {
        this.loginInput = loginInput;
    }

    @Nullable
    public String getPasswordInput() {
        return passwordInput;
    }

    public void setPasswordInput(@Nullable final String passwordInput) {
        this.passwordInput = passwordInput;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(@Nullable String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            loggedUser = userService.login(loginInput, passwordInput);
        } catch (IncorrectDataException e) {
            e.printStackTrace();
            final FacesMessage errorMessage = new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Failure",
                    "invalid combination of user and  password");
            context.addMessage(null, errorMessage);
            return;
        }
        final FacesMessage successMessage = new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Success",
                "logged in");
        context.addMessage(null, successMessage);
        clearFields();
    }

    public String logout() {
        System.out.println("jsf-logout");
        loggedUser = null;
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Success", "logged out"));
        return "index";
    }

    public void register() {
        System.out.println("jsf-register");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            userService.register(loginInput, passwordInput);
            final FacesMessage successMessage = new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Success",
                    "user created");
            context.addMessage(null, successMessage);
        } catch (IncorrectDataException e) {
            e.printStackTrace();
            final FacesMessage errorMessage = new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Error",
                    e.getMessage());
            context.addMessage(null, errorMessage);
        }
        clearFields();
    }

    private void clearFields() {
        loginInput = null;
        passwordInput = null;
        passwordConfirmation = null;
    }

}
