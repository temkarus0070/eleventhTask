package chatApp.services.persistence;

import chatApp.domain.User;
import chatApp.services.persistence.interfaces.UserRepository;

import java.util.Collection;
import java.util.Set;

public class InMemoryUserStorage implements UserRepository {
    private static InMemoryUserStorage inMemoryUserStorage;
    private static Set<User> userSet;
    private InMemoryUserStorage(){

    }

    public static InMemoryUserStorage getInstance(){
        if(inMemoryUserStorage==null){
            inMemoryUserStorage=new InMemoryUserStorage();
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
}
