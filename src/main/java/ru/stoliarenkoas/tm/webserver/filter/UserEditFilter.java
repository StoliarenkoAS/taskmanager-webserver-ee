package ru.stoliarenkoas.tm.webserver.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.stoliarenkoas.tm.webserver.controller.UserController;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@javax.servlet.annotation.WebFilter("/user/edit")
public class UserEditFilter extends SpringBeanAutowiringSupport implements Filter {

    private UserController userController;
    @Autowired
    public void setAuthorizationController(UserController userController) {
        this.userController = userController;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        if (userController.getEditableUser() != null) chain.doFilter(request, response);
        final FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            context.addMessage(null, new FacesMessage("Error", "No user to edit!"));
        }
        if (response instanceof HttpServletResponse) {
            ((HttpServletResponse)response).sendRedirect("/user/list");
            return;
        }
        response.getWriter().println("No user to edit!");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
