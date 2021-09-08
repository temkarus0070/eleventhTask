package chatApp.services.persistence;

import chatApp.domain.User;

import java.util.Optional;

public interface PersistenceUserService {
    public Optional<User> getUser(int userId);
    public Optional<User> getUserByName(String name);
    public void updateUser(User user);
    public void deleteUser(int userId);
    public void deleteUserByName(String name);
}
