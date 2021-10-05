package chatApp.services.persistence.implementation;

import chatApp.domain.User;
import chatApp.services.persistence.interfaces.PersistenceUserService;
import chatApp.services.persistence.interfaces.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PersistenceUserServiceImpl implements PersistenceUserService {
    private UserRepository userRepository;

    public PersistenceUserServiceImpl(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    @Override
    public void addUser(User user) {
        userRepository.add(user);
    }

    @Override
    public Optional<User> getUser(String userName) {
        return userRepository.get().stream().filter(user -> user.getName().equals(userName)).findFirst();
    }



    @Override
    public Collection<User> get() {
        return userRepository.get();
    }

    @Override
    public void updateUser(User user) {
        userRepository.add(user);
    }

    @Override
    public void deleteUser(String username) {
        getUser(username).ifPresent(user -> userRepository.delete(user));
    }

}
