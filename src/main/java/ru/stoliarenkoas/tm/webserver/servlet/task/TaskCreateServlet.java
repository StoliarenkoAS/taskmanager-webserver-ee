package ru.stoliarenkoas.tm.webserver.servlet.task;

import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.entity.Task;
import ru.stoliarenkoas.tm.webserver.service.SessionServiceImpl;
import ru.stoliarenkoas.tm.webserver.service.TaskServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

@WebServlet(urlPatterns = "/task-create")
public class TaskCreateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Session session = new SessionServiceImpl().getById((String) req.getSession().getAttribute(Attributes.SESSION_ID));
            if (session == null) resp.sendRedirect("/");

            final Task task = new Task();
            final String projectId = req.getParameter(Attributes.PROJECT_ID);
            final String taskName = req.getParameter(Attributes.NAME);
            final String taskDescription = req.getParameter(Attributes.DESCRIPTION);
            final String startDate = req.getParameter(Attributes.START_DATE);
            final String endDate = req.getParameter(Attributes.END_DATE);

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            task.setUserId(session.getUserId());
            task.setProjectId(projectId);
            task.setName(taskName);
            task.setDescription(taskDescription);
            task.setStartDate(format.parse(startDate));
            task.setEndDate(format.parse(endDate));

            new TaskServiceImpl().save(session, task);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.sendRedirect("tasks");
    }

}
