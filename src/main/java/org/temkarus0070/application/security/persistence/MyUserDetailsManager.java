package org.temkarus0070.application.security.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.services.persistence.UserStorage;


@Component
public class MyUserDetailsManager implements UserDetailsManager {
    private UserStorage userStorage;

    @Autowired
    public void setUserStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void createUser(UserDetails user) {
        User user1 = new User(user);
        userStorage.add(user1);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {
        userStorage.delete(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userStorage.get(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userStorage.get(username);
        if (user != null)
            return new MyUserDetails(user);
        else throw new UsernameNotFoundException("user with login " + username + " wasnt found");
    }
}
