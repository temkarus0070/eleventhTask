package org.temkarus0070.application.facades;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import java.util.Optional;

@Component
public class AuthFacade {
    private final PersistenceUserService persistenceUserService;
    private final PasswordEncoder passwordEncoder;

    public AuthFacade(PersistenceUserService persistenceUserService, @Lazy PasswordEncoder passwordEncoder) {
        this.persistenceUserService = persistenceUserService;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles("ROLE_USER");
            persistenceUserService.addUser(user);
    }

    public boolean login(User user){
        Optional<User> user1 = persistenceUserService.getUser(user.getUsername());
        if (user1.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), user1.get().getPassword())) {
                return true;
            }
        }
        return false;
    }
}
