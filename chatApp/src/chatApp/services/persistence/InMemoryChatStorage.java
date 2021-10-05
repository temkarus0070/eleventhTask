package chatApp.services.persistence;



import chatApp.domain.chat.Chat;
import chatApp.services.persistence.interfaces.ChatRepository;

import java.util.Collection;
import java.util.HashSet;

public class InMemoryChatStorage implements ChatRepository {
    private static int maxId=0;
    private static InMemoryChatStorage inMemoryChatStorage;

    private InMemoryChatStorage(){

    }

    public static InMemoryChatStorage getInstance(){
        if(inMemoryChatStorage ==null){
            inMemoryChatStorage =new InMemoryChatStorage();
            chats= new HashSet<>();
        }
        return inMemoryChatStorage;
    }

    private static Collection<Chat> chats;

    @Override
    public Collection<Chat> get() {
        return chats;
    }


    @Override
    public void update(Chat entity) {
        Chat chat=get().stream().filter(chat1 -> chat1.getId()==entity.getId()).findFirst().get();
        if(chat!=null) {
            chats.remove(chat);
            chats.add(entity);
        }

    }

    @Override
    public void add(Chat chat){
        chat.setId(maxId);
        chats.add(chat);
        maxId++;
    }

    @Override
    public void delete(Chat chat) {
        chats.remove(chat);
    }
}
