package org.temkarus0070.web.RestControllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.facades.UserFacade;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
   private UserFacade userFacade;

    public UserRestController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping("/allUsersExceptMe")
    public List<User> allUsersExceptMe() {
        return this.userFacade.findAllUsersExceptMe();
    }
}
