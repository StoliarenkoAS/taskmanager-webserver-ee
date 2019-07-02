package ru.stoliarenkoas.tm.webserver.servlet.task;

import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.Status;
import ru.stoliarenkoas.tm.webserver.api.service.TaskService;
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

@WebServlet(urlPatterns = "/task-edit")
public class TaskEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Session session = new SessionServiceImpl().getById((String) req.getSession().getAttribute(Attributes.SESSION_ID));
            if (session == null) resp.sendRedirect("/");

            final Task task = new TaskServiceImpl().get(session, req.getParameter(Attributes.TASK_ID));
            if (task == null) return;
            req.setAttribute(Attributes.TASK, task);
            req.getRequestDispatcher("/WEB-INF/view/task-edit.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Session session = new SessionServiceImpl().getById((String)req.getSession().getAttribute(Attributes.SESSION_ID));

            final TaskService taskService = new TaskServiceImpl();
            final Task task = taskService.get(session, req.getParameter(Attributes.TASK_ID));
            if (task == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

            task.setName(req.getParameter(Attributes.NAME));
            task.setDescription(req.getParameter(Attributes.DESCRIPTION));
            task.setStatus(Status.valueOf(req.getParameter(Attributes.STATUS)));
            task.setStartDate(format.parse(req.getParameter(Attributes.START_DATE)));
            task.setEndDate(format.parse(req.getParameter(Attributes.END_DATE)));

            taskService.save(session, task);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.sendRedirect("tasks");
    }

}
