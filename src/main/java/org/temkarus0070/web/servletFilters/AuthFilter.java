package org.temkarus0070.web.servletFilters;

import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.temkarus0070.application.domain.exceptions.ChatAppException;
import org.temkarus0070.application.services.AuthService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Component
@WebFilter(urlPatterns = "/*")
public class AuthFilter implements Filter {
    private AuthService authService;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (authService == null) {
            ServletContext servletContext = servletRequest.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            authService = webApplicationContext.getBean(AuthService.class);
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getRequestURI().contains("register") || request.getRequestURI().contains("login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try {
            if (!authService.isAuthorized(request.getCookies())) {
                servletRequest.getRequestDispatcher("/login").forward(servletRequest, servletResponse);
                return;
            } else {
                servletRequest.setAttribute("isAuthorized", true);
            }
        } catch (ChatAppException e) {
            servletResponse.getOutputStream().write(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
