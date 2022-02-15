package org.temkarus0070.web.servlets.auth;

import org.temkarus0070.application.domain.exceptions.ChatAppException;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.AuthServiceImpl;
import org.temkarus0070.application.services.PasswordEncoderImpl;
import org.temkarus0070.application.services.persistence.UserStorage;
import org.temkarus0070.application.services.persistence.implementation.PersistenceUserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RegisterServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        authService=new AuthServiceImpl(new PersistenceUserServiceImpl(new UserStorage()),new PasswordEncoderImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String username = req.getParameterMap().get("username")[0];
            String password = req.getParameterMap().get("password")[0];
            if (authService.register(username, password)) {
                resp.addCookie(new Cookie("username", username));
                resp.addCookie(new Cookie("password", password));
                resp.sendRedirect("/");
            }
        } catch (ChatAppException usernameAlreadyExistException) {
            resp.getOutputStream().write((usernameAlreadyExistException.getMessage().getBytes(StandardCharsets.UTF_8)));

        }
    }
}
