package ru.stoliarenkoas.tm.webserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.api.service.UserService;
import ru.stoliarenkoas.tm.webserver.model.dto.SessionDTO;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/authorization")
public class AuthorizationController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestParam("login") String login, @RequestParam("password") String password, HttpSession httpSession) {
        System.out.println("user-login");
        try {
            final SessionDTO session = userService.login(login, password);
            httpSession.setAttribute(Attributes.SESSION_ID, session.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }

    @PostMapping("/logout")
    public String logout(HttpSession httpSession) {
        System.out.println("user-logout");
        try {
            httpSession.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }

    @PostMapping("/register")
    public String register(@RequestParam("login") String login, @RequestParam("password") String password) {
        System.out.println("user-register");
        try {
            userService.register(login, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }


}
