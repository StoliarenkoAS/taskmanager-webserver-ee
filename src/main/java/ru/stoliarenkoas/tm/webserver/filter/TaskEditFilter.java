package ru.stoliarenkoas.tm.webserver.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.stoliarenkoas.tm.webserver.controller.TaskController;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@javax.servlet.annotation.WebFilter("/task/edit")
public class TaskEditFilter extends SpringBeanAutowiringSupport implements Filter {

    private TaskController taskController;
    @Autowired
    public void setAuthorizationController(TaskController taskController) {
        this.taskController = taskController;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        if (taskController.getEditableTask() != null) chain.doFilter(request, response);
        final FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            context.addMessage(null, new FacesMessage("Error", "No task to edit!"));
        }
        if (response instanceof HttpServletResponse) {
            ((HttpServletResponse)response).sendRedirect("/task/list");
            return;
        }
        response.getWriter().println("No task to edit!");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
