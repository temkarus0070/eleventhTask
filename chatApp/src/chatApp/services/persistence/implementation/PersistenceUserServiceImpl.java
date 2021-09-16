package chatApp.services.persistence.implementation;

import chatApp.domain.User;
import chatApp.services.persistence.interfaces.PersistenceUserService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PersistenceUserServiceImpl implements PersistenceUserService {
    private static Set<User> users=new HashSet<>();

    public PersistenceUserServiceImpl() {

    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public Optional<User> getUser(String userName) {
        return users.stream().filter(user -> user.getName().equals(userName)).findFirst();
    }

    @Override
    public Optional<User> getUserByName(String name) {
        return users.stream().filter(user -> user.getName().equals(name)).findFirst();
    }

    @Override
    public Collection<User> get() {
        return users;
    }

    @Override
    public void updateUser(User user) {
        users.add(user);
    }

    @Override
    public void deleteUser(String username) {
        getUser(username).ifPresent(user -> users.remove(user));
    }

    @Override
    public void deleteUserByName(String name) {
        getUserByName(name).ifPresent(user -> users.remove(user));
    }
}
