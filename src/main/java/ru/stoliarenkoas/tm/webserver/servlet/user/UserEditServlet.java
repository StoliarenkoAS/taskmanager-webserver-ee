package ru.stoliarenkoas.tm.webserver.servlet.user;

import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.Status;
import ru.stoliarenkoas.tm.webserver.api.service.UserService;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.entity.User;
import ru.stoliarenkoas.tm.webserver.service.SessionServiceImpl;
import ru.stoliarenkoas.tm.webserver.service.UserServiceImpl;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

@WebServlet(urlPatterns = "/user-edit")
public class UserEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Session session = new SessionServiceImpl().getById((String) req.getSession().getAttribute(Attributes.SESSION_ID));
            final User user = new UserServiceImpl().get(session, req.getParameter(Attributes.USER_ID));
            if (user == null) return;
            req.setAttribute(Attributes.USER, user);
            req.getRequestDispatcher("/WEB-INF/view/user-edit.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Session session = new SessionServiceImpl().getById((String)req.getSession().getAttribute(Attributes.SESSION_ID));
            if (session == null) {
                resp.sendRedirect("/");
                return;
            }

            final UserService userService = new UserServiceImpl();
            final User user = userService.get(session, session.getUserId());
            if (user == null || User.Role.ADMIN != user.getRole()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            final User editableUser = userService.get(session, req.getParameter(Attributes.USER_ID));
            if (editableUser == null) {
                resp.sendRedirect("/");
                return;
            }
            editableUser.setLogin(req.getParameter(Attributes.LOGIN));
            editableUser.setPasswordHash(CypherUtil.getMd5(req.getParameter(Attributes.PASSWORD)));
            editableUser.setRole(User.Role.valueOf(req.getParameter(Attributes.ROLE)));

            userService.save(session, editableUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect("users");
    }

}
