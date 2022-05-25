package org.temkarus0070.web.RestControllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class RegistrationRestController {
    private AuthService authService;
    private PersistenceUserService persistenceUserService;
    private PasswordEncoder passwordEncoder;

    public RegistrationRestController(AuthService authService, PersistenceUserService persistenceUserService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.persistenceUserService = persistenceUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles("ROLE_USER");
            persistenceUserService.addUser(user);

        } catch (ChatAppDatabaseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @PostMapping("/login")
    public boolean login(@RequestBody User user) {
        try {
            Optional<User> user1 = persistenceUserService.getUser(user.getUsername());
            if (user1.isPresent()) {
                if (passwordEncoder.matches(user.getPassword(), user1.get().getPassword())) {
                    return true;
                }
            }
            return false;
        } catch (ChatAppDatabaseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }
}
