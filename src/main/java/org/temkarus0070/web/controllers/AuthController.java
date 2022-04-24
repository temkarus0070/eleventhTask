package org.temkarus0070.web.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AuthController {
    private AuthService authService;
    private PersistenceUserService persistenceUserService;
    private PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, PersistenceUserService persistenceUserService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.persistenceUserService = persistenceUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, HttpServletResponse resp, Model model) throws IOException {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles("ROLE_USER");
            persistenceUserService.addUser(user);
            return "redirect:/login";

        } catch (ChatAppDatabaseException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

    }


}
