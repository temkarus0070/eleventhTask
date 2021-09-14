package chatApp.services.persistence.interfaces;

import chatApp.domain.User;

import java.util.Collection;
import java.util.Optional;

public interface PersistenceUserService {
    public Optional<User> getUser(int userId);

    public Optional<User> getUserByName(String name);

    public Collection<User> get();

    public void updateUser(User user);

    public void deleteUser(int userId);

    public void deleteUserByName(String name);
}
