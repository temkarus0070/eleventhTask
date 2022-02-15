package org.temkarus0070.web.servlets.auth;

import org.temkarus0070.application.domain.exceptions.ChatAppException;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.AuthServiceImpl;
import org.temkarus0070.application.services.PasswordEncoderImpl;
import org.temkarus0070.application.services.persistence.UserStorage;
import org.temkarus0070.application.services.persistence.implementation.PersistenceUserServiceImpl;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private AuthService authService;
    private PersistenceUserService persistenceUserService;

    @Override
    public void init() throws ServletException {
        authService = new AuthServiceImpl(new PersistenceUserServiceImpl(new UserStorage()), new PasswordEncoderImpl());
        persistenceUserService = new PersistenceUserServiceImpl(new UserStorage());
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = "";
        String password = "";
        Map<String, String[]> params = req.getParameterMap();
        try {
            username = params.get("username")[0];
            password = params.get("password")[0];
            try {
                if (authService.login(username, password)) {
                    Cookie usernameCookie = new Cookie("username", username);
                    usernameCookie.setMaxAge(999999);
                    Cookie passwordCookie = new Cookie("password", password);
                    passwordCookie.setMaxAge(999999);

                    resp.addCookie(usernameCookie);
                    resp.addCookie(passwordCookie);

                    resp.sendRedirect("/");
                } else {
                    resp.getOutputStream().write("auth error".getBytes(StandardCharsets.UTF_8));
                }
            } catch (ChatAppException chatAppException) {
                resp.getOutputStream().write(chatAppException.getMessage().getBytes(StandardCharsets.UTF_8));
            }

        } catch (Exception ex) {
            resp.getOutputStream().write(ex.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }
}
