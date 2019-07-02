package ru.stoliarenkoas.tm.webserver.servlet.task;

import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.service.ProjectServiceImpl;
import ru.stoliarenkoas.tm.webserver.service.SessionServiceImpl;
import ru.stoliarenkoas.tm.webserver.service.TaskServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/task-remove")
public class TaskRemoveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Session session = new SessionServiceImpl().getById((String) req.getSession().getAttribute(Attributes.SESSION_ID));
            if (session == null) resp.sendRedirect("/");

            new TaskServiceImpl().delete(session, req.getParameter(Attributes.TASK_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect("tasks");
    }

}
