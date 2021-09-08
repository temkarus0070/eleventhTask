package chatApp.services.persistence;

import chatApp.domain.chat.Chat;
import java.util.*;

public class PersistenceChatServiceImpl implements PersistenceChatService{
    private Set<Chat> chats;

    public PersistenceChatServiceImpl(){
        chats=new HashSet<>();
    }


    @Override
    public Optional<Chat> getChat(int id) {
        return chats.stream().filter(chat->chat.getId()==id).findFirst();
    }

    @Override
    public void removeChat(int id) {
        Optional<Chat>chat=getChat(id);
        chat.ifPresent(value -> chats.remove(value));

    }

    @Override
    public void updateChat(Chat chat) {
        chats.add(chat);
    }

    @Override
    public void addChat(Chat chat) {
        chats.add(chat);
    }
}
