package ru.stoliarenkoas.tm.webserver.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.stoliarenkoas.tm.webserver.controller.AuthorizationController;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@javax.servlet.annotation.WebFilter(
        urlPatterns = {
                "/user/*",
                "/project/*",
                "/task/*",
        }
)
public class UnauthorizedFilter extends SpringBeanAutowiringSupport implements Filter {

    private AuthorizationController authorizationController;
    @Autowired
    public void setAuthorizationController(AuthorizationController authorizationController) {
        this.authorizationController = authorizationController;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        if (authorizationController.getLoggedUser() != null) chain.doFilter(request, response);
        if (response instanceof HttpServletResponse) {
            ((HttpServletResponse)response).sendRedirect("/");
            return;
        }
        response.getWriter().println("Access denied!");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
