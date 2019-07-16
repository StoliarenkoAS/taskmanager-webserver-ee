package ru.stoliarenkoas.tm.webserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.api.service.UserService;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/authorization")
public class AuthorizationController {

    @PostMapping("/login")
    public String login(@RequestParam("login") String login, @RequestParam("password") String password, HttpSession httpSession) {
        System.out.println("user-login");
        try {
            final Session session = new UserServiceImpl().login(login, password);
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
            final UserService userService = new UserServiceImpl();
            userService.register(login, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }


}
