package ru.stoliarenkoas.tm.webserver.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.stoliarenkoas.tm.webserver.controller.AuthorizationController;
import ru.stoliarenkoas.tm.webserver.controller.ProjectController;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@javax.servlet.annotation.WebFilter("/project/edit")
public class ProjectEditFilter extends SpringBeanAutowiringSupport implements Filter {

    private ProjectController projectController;
    @Autowired
    public void setAuthorizationController(ProjectController projectController) {
        this.projectController = projectController;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        if (projectController.getEditableProject() != null) chain.doFilter(request, response);
        final FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            context.addMessage(null, new FacesMessage("Error", "No project to edit!"));
        }
        if (response instanceof HttpServletResponse) {
            ((HttpServletResponse)response).sendRedirect("/project/list");
            return;
        }
        response.getWriter().println("No project to edit!");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
