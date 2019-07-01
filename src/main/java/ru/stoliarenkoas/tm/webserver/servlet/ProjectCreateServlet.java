package ru.stoliarenkoas.tm.webserver.servlet;

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
import java.text.SimpleDateFormat;

@WebServlet(urlPatterns = "/project-create")
public class ProjectCreateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Session session = new SessionServiceImpl().getById((String) req.getSession().getAttribute(Attributes.SESSION_ID));
            if (session == null) resp.sendRedirect("/");

            final Project project = new Project();
            final String projectName = req.getParameter(Attributes.NAME);
            final String projectDescription = req.getParameter(Attributes.DESCRIPTION);
            final String startDate = req.getParameter(Attributes.START_DATE);
            final String endDate = req.getParameter(Attributes.END_DATE);

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            project.setUserId(session.getUserId());
            project.setName(projectName);
            project.setDescription(projectDescription);
            project.setStartDate(format.parse(startDate));
            project.setEndDate(format.parse(endDate));

            new ProjectServiceImpl().save(session, project);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.sendRedirect("projects");
    }

}
