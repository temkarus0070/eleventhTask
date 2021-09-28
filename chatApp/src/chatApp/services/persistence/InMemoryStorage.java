package chatApp.services.persistence;

import chatApp.domain.chat.Chat;

import java.util.HashSet;
import java.util.Set;

public class InMemoryStorage {
    private static InMemoryStorage inMemoryStorage;

    public InMemoryStorage getInstance(){
        if(inMemoryStorage==null){
            inMemoryStorage=new InMemoryStorage();
            chats=new HashSet<>();
        }
        return inMemoryStorage;
    }

    private Set<Chat> chats;

    public Set<Chat> getChats() {
        return chats;
    }

    public void add(Chat chat){
        chats.add(chat);
    }
}
