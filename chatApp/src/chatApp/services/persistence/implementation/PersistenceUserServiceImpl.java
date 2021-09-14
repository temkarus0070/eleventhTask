package chatApp.services.persistence.implementation;

import chatApp.domain.User;
import chatApp.services.persistence.interfaces.PersistenceUserService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PersistenceUserServiceImpl implements PersistenceUserService {
    private Set<User> users;

    public PersistenceUserServiceImpl() {
        users = new HashSet<>();
    }

    @Override
    public Optional<User> getUser(int userId) {
        return users.stream().filter(user -> user.getId() == userId).findFirst();
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
    public void deleteUser(int userId) {
        getUser(userId).ifPresent(user -> users.remove(user));
    }

    @Override
    public void deleteUserByName(String name) {
        getUserByName(name).ifPresent(user -> users.remove(user));
    }
}
