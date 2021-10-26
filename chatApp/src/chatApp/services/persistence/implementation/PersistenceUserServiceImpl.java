package chatApp.services.persistence.implementation;

import chatApp.domain.User;
import chatApp.domain.exceptions.UsernameAlreadyExistException;
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
    public void addUser(User user) throws Exception{
        Optional<User> userOptional=getUser(user.getName());
        if(!userOptional.isPresent())
            userRepository.add(user);
        else throw new UsernameAlreadyExistException();
    }

    @Override
    public Optional<User> getUser(String userName)throws Exception {
        return userRepository.get().stream().filter(user -> user.getName().equals(userName)).findFirst();
    }



    @Override
    public Collection<User> get()throws Exception {
        return userRepository.get();
    }

    @Override
    public void updateUser(User user)throws Exception {
        userRepository.update(user);
    }

    @Override
    public void deleteUser(String username)throws Exception {
        userRepository.delete(username);
    }

    @Override
    public Collection<User> getUsersNotAtThatChat(Integer chatId) throws Exception {
        return userRepository.getUsersNotAtThatChat(chatId);
    }

}
