package chatApp.services.persistence;



import chatApp.services.persistence.interfaces.Repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class InMemoryStorage<T> implements Repository<T> {
    private static InMemoryStorage inMemoryStorage;

    public InMemoryStorage getInstance(){
        if(inMemoryStorage==null){
            inMemoryStorage=new InMemoryStorage();
            chats=new HashSet<T>();
        }
        return inMemoryStorage;
    }

    private Set<T> chats;

    @Override
    public Collection<T> get() {
        return chats;
    }

    @Override
    public void add(T chat){
        chats.add(chat);
    }

    @Override
    public void delete(T chat) {
        chats.remove(chat);
    }
}
