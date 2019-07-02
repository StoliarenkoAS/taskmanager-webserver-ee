package ru.stoliarenkoas.tm.webserver.servlet.project;

import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectService;
import ru.stoliarenkoas.tm.webserver.entity.Project;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.service.ProjectServiceImpl;
import ru.stoliarenkoas.tm.webserver.service.SessionServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@WebServlet(urlPatterns = "/project-remove")
public class ProjectRemoveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Session session = new SessionServiceImpl().getById((String) req.getSession().getAttribute(Attributes.SESSION_ID));
            if (session == null) resp.sendRedirect("/");

            new ProjectServiceImpl().delete(session, req.getParameter(Attributes.PROJECT_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect("projects");
    }

}
