package ru.stoliarenkoas.tm.webserver.servlet.user;

import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.api.service.UserService;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.entity.User;
import ru.stoliarenkoas.tm.webserver.service.SessionServiceImpl;
import ru.stoliarenkoas.tm.webserver.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@WebServlet(urlPatterns = "/users")
public class UserListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Session session = new SessionServiceImpl().getById((String) req.getSession().getAttribute(Attributes.SESSION_ID));
            if (session == null) {
                resp.sendRedirect("/");
                return;
            }
            final UserService userService = new UserServiceImpl();
            final User user = userService.get(session, session.getUserId());
            if (user == null || User.Role.ADMIN!=user.getRole()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            final Collection<User> userList = userService.getAll(session);
            req.setAttribute(Attributes.USER_LIST, userList);
            req.getRequestDispatcher("/WEB-INF/view/users.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
