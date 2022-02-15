package org.temkarus0070.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.exceptions.*;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AuthServiceImpl implements AuthService {

    private PersistenceUserService persistenceUserService;

    private PasswordEncoder passwordEncoder;

    private Logger myLogger = Logger.getLogger(AuthServiceImpl.class.getName());


    @Autowired
    public AuthServiceImpl(PersistenceUserService persistenceUserService, PasswordEncoder passwordEncoder) {
        this.persistenceUserService = persistenceUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isAuthorized(Cookie[] cookies) throws ChatAppException {
        String username = "";
        String password = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                } else if (cookie.getName().equals("password")) {
                    password = cookie.getValue();
                }
            }
            return login(username, password);
        }
        return false;
    }


    @Override
    public boolean login(String username, String password) throws ChatAppException {
        User user = null;
        Optional<User> userOptional = persistenceUserService.getUser(username);
        if (userOptional.isPresent()) {
            String hashPassword = passwordEncoder.getHashFromPassword(password);
            if (!(userOptional.get().getPassword().equals(hashPassword) ||
                    userOptional.get().getPassword().equals(password))) {
                myLogger.log(Level.SEVERE, new InvalidAuthDataException().getMessage());
                throw new ChatAppException(new InvalidAuthDataException());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean register(String username, String password) throws ChatAppException {
        String hashPassword = passwordEncoder.getHashFromPassword(password);
        User user = new User(username, hashPassword);
        try {
            persistenceUserService.addUser(user);
        } catch (ChatAppDatabaseException chatAppDatabaseException) {
            myLogger.log(Level.SEVERE, chatAppDatabaseException.getMessage());
            throw new ChatAppException(chatAppDatabaseException);
        }
        return true;
    }

    @Override
    public User getCurrentUser(Cookie[] cookies) throws ChatAppException {
        Cookie cookie = Arrays.stream(cookies).filter(cookie1 -> cookie1.getName().equals("username")).findFirst().get();
        if (cookie != null) {
            return persistenceUserService.getUser(cookie.getValue()).get();
        } else {
            myLogger.log(Level.SEVERE, "User not exists");
            throw new ChatAppException(new UserNotExistsException());
        }
    }
}
