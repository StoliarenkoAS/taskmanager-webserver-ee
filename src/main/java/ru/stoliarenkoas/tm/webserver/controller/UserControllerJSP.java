package ru.stoliarenkoas.tm.webserver.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserControllerJSP {

    private UserServicePageable userService;

    @Autowired
    public void setUserService(UserServicePageable userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String getUserList(
            @NotNull final Model model,
            @RequestParam(name = Attributes.PAGE, required = false) Integer pageNumber,
            @NotNull final HttpSession httpSession) {
        System.out.println("user-list");
        try{
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null || UserDTO.Role.ADMIN != user.getRole()) return "index";

            if (pageNumber == null) pageNumber = 0;
            model.addAttribute(Attributes.PAGE, pageNumber);
            final PageRequest page = PageRequest.of(pageNumber, 1);
            final Page<UserDTO> userList = userService.findAll(loggedUserId, page);
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
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null || UserDTO.Role.ADMIN != user.getRole()) throw new Exception("forbidden action");

            final UserDTO newUser = new UserDTO();
            newUser.setLogin(requestParams.get(Attributes.LOGIN));
            newUser.setPasswordHash(CypherUtil.getMd5(requestParams.get(Attributes.PASSWORD)));
            newUser.setRole(UserDTO.Role.valueOf(requestParams.get(Attributes.ROLE)));
            System.out.println(newUser);

            userService.persist(loggedUserId, newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RedirectView("/user/list");
    }

    @GetMapping("/edit")
    public String getUserEditPage(
            @NotNull final Model model,
            @RequestParam(Attributes.USER_ID) String userId,
            @NotNull final HttpSession httpSession) {
        System.out.println("user-edit");
        try {
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, userId);
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
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null || UserDTO.Role.ADMIN != user.getRole()) throw new Exception("forbidden action");

            final UserDTO editableUser = userService.findOne(loggedUserId, requestParams.get(Attributes.USER_ID));
            if (editableUser == null) throw new Exception("invalid user");

            editableUser.setLogin(requestParams.get(Attributes.LOGIN));
            editableUser.setPasswordHash(CypherUtil.getMd5(requestParams.get(Attributes.PASSWORD)));
            editableUser.setRole(UserDTO.Role.valueOf(requestParams.get(Attributes.ROLE)));

            userService.persist(loggedUserId, editableUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RedirectView("/user/list");
    }

    @PostMapping("/remove")
    public RedirectView removeUser(@RequestParam(Attributes.USER_ID) String userId, HttpSession httpSession) {
        try {
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null || UserDTO.Role.ADMIN != user.getRole()) throw new Exception("forbidden action");

            userService.remove(loggedUserId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RedirectView("/user/list");
    }

}
