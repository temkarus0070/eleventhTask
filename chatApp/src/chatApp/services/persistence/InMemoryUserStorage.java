package chatApp.services.persistence;

import chatApp.domain.User;
import chatApp.services.persistence.interfaces.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class InMemoryUserStorage implements UserRepository {
    private static InMemoryUserStorage inMemoryUserStorage;
    private static Set<User> userSet;
    private InMemoryUserStorage(){

    }

    public static InMemoryUserStorage getInstance(){
        if(inMemoryUserStorage==null){
            inMemoryUserStorage=new InMemoryUserStorage();
        }
        if(userSet==null){
            userSet=new HashSet<>();
        }
        return inMemoryUserStorage;
    }

    @Override
    public Collection<User> get() {
        return userSet;
    }

    @Override
    public void add(User entity) {
        userSet.add(entity);
    }

    @Override
    public void delete(User entity) {
        userSet.remove(entity);
    }

    @Override
    public void update(User entity) {
        User user=get().stream().filter(user1 -> user1.getName().equals(entity.getName())).findFirst().get();
        if(user!=null){
            userSet.remove(user);
            userSet.add(entity);
        }
    }
}
