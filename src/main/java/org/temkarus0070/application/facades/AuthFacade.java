package org.temkarus0070.application.facades;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.User;

@Component
public class AuthFacade {
    public User getCurrentAppUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return new User(authentication.getName());
    }
}
