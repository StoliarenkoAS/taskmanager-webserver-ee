package ru.stoliarenkoas.tm.webserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/authorization")
public class AuthorizationController {

    private UserServicePageable userService;
    @Autowired
    public void setUserService(UserServicePageable userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestParam("login") String login, @RequestParam("password") String password, HttpSession httpSession) {
        System.out.println("user-login");
        try {
            final UserDTO userDTO = userService.login(login, password);
            if (userDTO == null) return null;
            httpSession.setAttribute(Attributes.USER_ID, userDTO.getId());
            httpSession.setAttribute(Attributes.LOGIN, userDTO.getLogin());
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
