package chatApp.services.persistence;

import chatApp.domain.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PersistenceUserServiceImpl implements PersistenceUserService{
    private Set<User> users;

    public PersistenceUserServiceImpl(){
        users=new HashSet<>();
    }

    @Override
    public Optional<User> getUser(int userId) {
        return users.stream().filter(user-> user.getId()==userId).findFirst();
    }

    @Override
    public Optional<User> getUserByName(String name) {
        return users.stream().filter(user-> user.getName().equals(name)).findFirst();
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
