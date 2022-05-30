package org.temkarus0070.web.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.facades.AuthFacade;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AuthController {
    private AuthFacade authFacade;

    public AuthController(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user,  Model model) throws IOException {
        try {
            authFacade.register(user);
            return "redirect:/login";

        } catch (ChatAppDatabaseException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

    }


}
