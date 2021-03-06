package servletFilters;

import chatApp.domain.exceptions.ChatAppException;
import chatApp.services.AuthService;
import chatApp.services.AuthServiceImpl;
import chatApp.services.PasswordEncoderImpl;
import chatApp.services.persistence.InMemoryUserStorage;
import chatApp.services.persistence.UserStorage;
import chatApp.services.persistence.implementation.PersistenceUserServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthFilter extends HttpFilter {

    AuthService authService;

    @Override
    public void init() throws ServletException {
        authService=new AuthServiceImpl(new PersistenceUserServiceImpl(new UserStorage()),new PasswordEncoderImpl());
        super.init();
    }

    @Override

    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getRequestURI().contains("register") || req.getRequestURI().contains("login")) {
            super.doFilter(req, res, chain);
            return;
        }
        try {
            if (!authService.isAuthorized(req.getCookies())) {
                req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
                return;
            } else {
                req.setAttribute("isAuthorized", true);
            }
        } catch (ChatAppException e) {
            res.getOutputStream().write(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
        super.doFilter(req, res, chain);

    }
}
