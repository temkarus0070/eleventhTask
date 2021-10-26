package chatApp.services.persistence;

import chatApp.domain.User;
import chatApp.services.PasswordEncoderImpl;
import chatApp.services.persistence.interfaces.UserRepository;

import java.util.Collection;
import java.util.HashSet;
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
        if(userSet==null) {
            userSet = new HashSet<>();
            initialize();
        }

        return inMemoryUserStorage;
    }

    private static void initialize(){
        userSet.add(new User("temkarus0070",new PasswordEncoderImpl().getHashFromPassword("1234")));

    }

    @Override
    public Collection<User> get() {
        return userSet;
    }

    @Override
    public User get(String s) {
        return null;
    }

    @Override
    public void add(User entity) {
        userSet.add(entity);
    }

    @Override
    public void delete(String entity) {
        userSet.remove(entity);
    }

    @Override
    public void update(User entity) {
        User user=get().stream().filter(e->e.getName().equals(entity.getName())).findFirst().get();
        if(user!=null){
            userSet.remove(user);
            userSet.add(entity);
        }
    }

    @Override
    public Collection<User> getUsersNotAtThatChat(Integer chatId) throws Exception {
        return null;
    }
}
