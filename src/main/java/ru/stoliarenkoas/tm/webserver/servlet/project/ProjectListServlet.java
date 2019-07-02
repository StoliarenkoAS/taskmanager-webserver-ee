package ru.stoliarenkoas.tm.webserver.servlet.project;

import ru.stoliarenkoas.tm.webserver.Attributes;
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
import java.util.Collection;

@WebServlet(urlPatterns = "/projects")
public class ProjectListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String sessionId = (String) req.getSession().getAttribute(Attributes.SESSION_ID);
        System.out.println(sessionId);
        if (sessionId == null) resp.sendRedirect("/");
        try {
            final Session session = new SessionServiceImpl().getById(sessionId);
            if (session == null) resp.sendRedirect("/");
            final Collection<Project> projectList = new ProjectServiceImpl().getAll(session);
            req.setAttribute(Attributes.PROJECT_LIST, projectList);
            req.getRequestDispatcher("/WEB-INF/view/projects.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
