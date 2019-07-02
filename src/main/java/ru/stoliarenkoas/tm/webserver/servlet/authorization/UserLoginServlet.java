package ru.stoliarenkoas.tm.webserver.servlet.authorization;

import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/user-login")
public class UserLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("user-login");
        final String login = req.getParameter("login");
        final String password = req.getParameter("password");
        try {
            final Session session = new UserServiceImpl().login(login, password);
            req.getSession().setAttribute(Attributes.SESSION_ID, session.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect("projects");
    }

}
