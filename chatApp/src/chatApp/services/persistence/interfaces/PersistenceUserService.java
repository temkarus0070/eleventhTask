package chatApp.services.persistence.interfaces;

import chatApp.domain.User;
import chatApp.domain.exceptions.UsernameAlreadyExistException;

import java.util.Collection;
import java.util.Optional;

public interface PersistenceUserService {
    public void addUser(User user)throws UsernameAlreadyExistException;
    public Optional<User> getUser(String userName);

    public Optional<User> getUserByName(String name);

    public Collection<User> get();

    public void updateUser(User user);

    public void deleteUser(String username);

    public void deleteUserByName(String name);
}
