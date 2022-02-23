package org.temkarus0070.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.domain.exceptions.ChatAppException;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AuthController {
    private AuthService authService;
    private PersistenceUserService persistenceUserService;

    public AuthController(AuthService authService, PersistenceUserService persistenceUserService) {
        this.authService = authService;
        this.persistenceUserService = persistenceUserService;
    }

    @GetMapping("/login")
    public String get() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletResponse resp) {
        try {
            if (authService.login(user.getUsername(), user.getPassword())) {
                Cookie usernameCookie = new Cookie("username", user.getUsername());
                usernameCookie.setMaxAge(999999);
                Cookie passwordCookie = new Cookie("password", user.getPassword());
                passwordCookie.setMaxAge(999999);

                resp.addCookie(usernameCookie);
                resp.addCookie(passwordCookie);
                resp.sendRedirect("/");
                return "home";
            } else return "login failed";
        } catch (ChatAppException | IOException exception) {
            return exception.getMessage();
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody User user, HttpServletResponse resp) {
        try {
            persistenceUserService.addUser(user);
            Cookie usernameCookie = new Cookie("username", user.getUsername());
            usernameCookie.setMaxAge(999999);
            Cookie passwordCookie = new Cookie("password", user.getPassword());
            passwordCookie.setMaxAge(999999);

            resp.addCookie(usernameCookie);
            resp.addCookie(passwordCookie);
            resp.sendRedirect("/");
            return "home";
        } catch (ChatAppDatabaseException | IOException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("username") || cookie.getName().equals("password")) {
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
        }
        resp.sendRedirect("/login");
        return "login";
    }

}
