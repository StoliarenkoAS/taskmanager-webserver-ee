package ru.stoliarenkoas.tm.webserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.api.service.UserService;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.entity.User;
import ru.stoliarenkoas.tm.webserver.service.SessionServiceImpl;
import ru.stoliarenkoas.tm.webserver.service.UserServiceImpl;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/list")
    public String getUserList(Model model, HttpSession httpSession) {
        System.out.println("user-list");
        try{
            final Session session = new SessionServiceImpl().getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) return "index";

            final UserService userService = new UserServiceImpl();
            final User user = userService.get(session, session.getUserId());
            if (user == null || User.Role.ADMIN != user.getRole()) return "index";

            final Collection<User> userList = userService.getAll(session);
            model.addAttribute(Attributes.USER_LIST, userList);
            return "users";
        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }
    }

    @PostMapping("/create")
    public RedirectView createUser(@RequestParam Map<String, String> requestParams, HttpSession httpSession) {
        System.out.println("create-user");
        try {
            final Session session = new SessionServiceImpl().getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) throw new Exception("not authorized");

            final UserService userService = new UserServiceImpl();
            final User user = userService.get(session, session.getUserId());
            if (user == null || User.Role.ADMIN != user.getRole()) throw new Exception("forbidden action");

            final User newUser = new User();
            newUser.setLogin(requestParams.get(Attributes.LOGIN));
            newUser.setPasswordHash(CypherUtil.getMd5(requestParams.get(Attributes.PASSWORD)));
            newUser.setRole(User.Role.valueOf(requestParams.get(Attributes.ROLE)));
            System.out.println(newUser);

            userService.save(session, newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RedirectView("/user/list");
    }

    @GetMapping("/edit")
    public String getUserEditPage(Model model, @RequestParam(Attributes.USER_ID) String userId, HttpSession httpSession) {
        System.out.println("user-edit");
        try {
            final Session session = new SessionServiceImpl().getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            final User user = new UserServiceImpl().get(session, userId);
            if (user == null) return "index";
            model.addAttribute(Attributes.USER, user);
            return "user-edit";
        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }
    }

    @PostMapping("/edit")
    public RedirectView editUser(@RequestParam Map<String, String> requestParams, HttpSession httpSession) {
        System.out.println("edit user");
        try {
            final Session session = new SessionServiceImpl().getById((String)httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) throw new Exception("not authorized");

            final UserService userService = new UserServiceImpl();
            final User user = userService.get(session, session.getUserId());
            if (user == null || User.Role.ADMIN != user.getRole()) throw new Exception("forbidden action");

            final User editableUser = userService.get(session, requestParams.get(Attributes.USER_ID));
            if (editableUser == null) throw new Exception("invalid user");

            editableUser.setLogin(requestParams.get(Attributes.LOGIN));
            editableUser.setPasswordHash(CypherUtil.getMd5(requestParams.get(Attributes.PASSWORD)));
            editableUser.setRole(User.Role.valueOf(requestParams.get(Attributes.ROLE)));

            userService.save(session, editableUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RedirectView("/user/list");
    }

    @PostMapping("/remove")
    public RedirectView removeUser(@RequestParam(Attributes.USER_ID) String userId, HttpSession httpSession) {
        try {
            final Session session = new SessionServiceImpl().getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) throw new Exception("not authorized");

            final UserService userService = new UserServiceImpl();
            final User user = userService.get(session, session.getUserId());
            if (user == null || User.Role.ADMIN != user.getRole()) throw new Exception("forbidden action");

            userService.delete(session, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RedirectView("/user/list");
    }

}
