package ru.stoliarenkoas.tm.webserver.servlet.task;

import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.entity.Project;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.entity.Task;
import ru.stoliarenkoas.tm.webserver.service.ProjectServiceImpl;
import ru.stoliarenkoas.tm.webserver.service.SessionServiceImpl;
import ru.stoliarenkoas.tm.webserver.service.TaskServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@WebServlet(urlPatterns = "/tasks")
public class TaskListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String sessionId = (String) req.getSession().getAttribute(Attributes.SESSION_ID);
        System.out.println(sessionId);
        if (sessionId == null) resp.sendRedirect("/");
        try {
            final Session session = new SessionServiceImpl().getById(sessionId);
            if (session == null) resp.sendRedirect("/");
            final Collection<Task> taskList = new TaskServiceImpl().getAll(session);
            req.setAttribute(Attributes.TASK_LIST, taskList);
            final Collection<Project> projectList = new ProjectServiceImpl().getAll(session);
            req.setAttribute(Attributes.PROJECT_LIST, projectList);
            req.getRequestDispatcher("/WEB-INF/view/tasks.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
