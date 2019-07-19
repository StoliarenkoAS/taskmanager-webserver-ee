package ru.stoliarenkoas.tm.webserver.controller.jsf;

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

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public void login() {
        System.out.println("jsf-login:" + loginInput + " - " + passwordInput);
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            loggedUser = userService.login(loginInput, passwordInput);
        } catch (IncorrectDataException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failure", "invalid combination of user and  password"));
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "logged in"));
        clearFields();
    }

    public void logout() {
        System.out.println("jsf-logout");
        loggedUser = null;
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Success", "logged out"));
    }

    public void register() {
        System.out.println("jsf-register");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            userService.register(loginInput, passwordInput);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "user created"));
        } catch (IncorrectDataException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", e.getMessage()));
        }
        clearFields();
    }

    private void clearFields() {
        loginInput = null;
        passwordInput = null;
        passwordConfirmation = null;
    }

    public void test() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",  "Your message: " + loginInput) );
        context.addMessage(null, new FacesMessage("Second Message", "Additional Message Detail"));
    }

}
