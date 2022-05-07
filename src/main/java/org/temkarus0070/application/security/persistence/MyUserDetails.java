package org.temkarus0070.application.security.persistence;

import org.springframework.security.core.GrantedAuthority;
import org.temkarus0070.application.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MyUserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRoles() != null) {
            String[] roles = user.getRoles().split(" ");
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : roles) {
                authorities.add((GrantedAuthority) () -> role);
            }
            return authorities;
        }
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
