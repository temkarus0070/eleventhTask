package org.temkarus0070.application.facades;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserFacade {
    private PersistenceUserService persistenceUserService;

    public UserFacade(PersistenceUserService persistenceUserService) {
        this.persistenceUserService = persistenceUserService;
    }

    public User getCurrentAppUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return new User(authentication.getName(),"*********",authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" ")));
    }


    public List<User> findAllUsersExceptMe() {
        return this.persistenceUserService.get().stream()
                .filter(e -> !e.getUsername().equals(getCurrentAppUser().getUsername()))
                .collect(Collectors.toList());
    }

}
