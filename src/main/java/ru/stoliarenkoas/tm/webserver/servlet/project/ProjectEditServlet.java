package ru.stoliarenkoas.tm.webserver.servlet.project;

import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.Status;
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
import java.text.SimpleDateFormat;

@WebServlet(urlPatterns = "/project-edit")
public class ProjectEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Session session = new SessionServiceImpl().getById((String) req.getSession().getAttribute(Attributes.SESSION_ID));
            final Project project = new ProjectServiceImpl().get(session, req.getParameter(Attributes.PROJECT_ID));
            if (project == null) return;
            req.setAttribute(Attributes.PROJECT, project);
            req.getRequestDispatcher("/WEB-INF/view/project-edit.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Session session = new SessionServiceImpl().getById((String)req.getSession().getAttribute(Attributes.SESSION_ID));
            if (session == null) resp.sendRedirect("/");

            final ProjectService projectService = new ProjectServiceImpl();
            final Project project = projectService.get(session, req.getParameter(Attributes.PROJECT_ID));
            if (project == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

            project.setName(req.getParameter(Attributes.NAME));
            project.setDescription(req.getParameter(Attributes.DESCRIPTION));
            project.setStatus(Status.valueOf(req.getParameter(Attributes.STATUS)));
            project.setStartDate(format.parse(req.getParameter(Attributes.START_DATE)));
            project.setEndDate(format.parse(req.getParameter(Attributes.END_DATE)));

            projectService.save(session, project);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect("projects");
    }

}
