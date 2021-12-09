package chatApp.services.persistence.implementation;

import chatApp.MyLogger;
import chatApp.domain.User;
import chatApp.domain.exceptions.ChatAppDatabaseException;
import chatApp.domain.exceptions.UsernameAlreadyExistException;
import chatApp.services.persistence.interfaces.PersistenceUserService;
import chatApp.services.persistence.interfaces.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

public class PersistenceUserServiceImpl implements PersistenceUserService {
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
            MyLogger.log(Level.SEVERE, String.format("user %s already exists", user.getName()));
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
