package org.temkarus0070.application.services.persistence.implementation;

import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.domain.exceptions.UsernameAlreadyExistException;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;
import org.temkarus0070.application.services.persistence.interfaces.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PersistenceUserServiceImpl implements PersistenceUserService {
    private Logger myLogger = Logger.getLogger(this.getClass().getName());
    private UserRepository userRepository;

    public PersistenceUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) throws ChatAppDatabaseException {
        Optional<User> userOptional = getUser(user.getName());
        if (userOptional.isEmpty())
            userRepository.add(user);
        else {
            myLogger.log(Level.SEVERE, String.format("user %s already exists", user.getName()));
            throw new ChatAppDatabaseException(new UsernameAlreadyExistException());
        }
    }

    @Override
    public Optional<User> getUser(String userName) throws ChatAppDatabaseException {
        return Optional.ofNullable(userRepository.get(userName));
    }


    @Override
    public Collection<User> get() throws ChatAppDatabaseException {
        return userRepository.get();
    }

    @Override
    public void updateUser(User user) throws ChatAppDatabaseException {
        userRepository.update(user);
    }

    @Override
    public void deleteUser(String username) throws ChatAppDatabaseException {
        userRepository.delete(username);
    }

    @Override
    public Collection<User> getUsersNotAtThatChat(Integer chatId) throws ChatAppDatabaseException {
        return userRepository.getUsersNotAtThatChat(chatId);
    }

}
