package chatApp.services.persistence.interfaces;

import chatApp.domain.User;
import chatApp.domain.exceptions.UsernameAlreadyExistException;

import java.util.Collection;
import java.util.Optional;

public interface PersistenceUserService {
    public void addUser(User user)throws UsernameAlreadyExistException,Exception;
    public Optional<User> getUser(String userName)throws Exception;



    public Collection<User> get()throws Exception;

    public void updateUser(User user)throws Exception;

    public void deleteUser(String username)throws Exception;

    public Collection<User> getUsersNotAtThatChat(Integer chatId)throws Exception;

}
