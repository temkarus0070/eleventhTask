package org.temkarus0070.web.RestControllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
    private PersistenceUserService persistenceUserService;

    public UserRestController(PersistenceUserService persistenceUserService) {
        this.persistenceUserService = persistenceUserService;
    }

    @GetMapping("/getAllUsersExceptMe")
    public List<User> findAllUsersExceptMe(Principal principal) {
        return this.persistenceUserService.get().stream()
                .filter(e -> !e.getUsername().equals(principal.getName()))
                .collect(Collectors.toList());
    }
}
