package org.temkarus0070.web.servletFilters;

import org.springframework.beans.factory.annotation.Autowired;
import org.temkarus0070.application.domain.exceptions.ChatAppException;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.AuthServiceImpl;
import org.temkarus0070.application.services.PasswordEncoderImpl;
import org.temkarus0070.application.services.persistence.UserStorage;
import org.temkarus0070.application.services.persistence.implementation.PersistenceUserServiceImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthFilter implements Filter {

    @Autowired
    private AuthService authService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        authService = new AuthServiceImpl(new PersistenceUserServiceImpl(new UserStorage()), new PasswordEncoderImpl());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getRequestURI().contains("register") || request.getRequestURI().contains("login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try {
            if (!authService.isAuthorized(request.getCookies())) {
                servletRequest.getRequestDispatcher("/jsp/login.jsp").forward(servletRequest, servletResponse);
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
