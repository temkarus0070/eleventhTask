package chatApp.services.persistence.implementation;

import chatApp.domain.chat.Chat;
import chatApp.domain.exceptions.ChatUpdateException;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.lang.reflect.Field;
import java.util.*;

public abstract class PersistenceChatServiceImpl implements PersistenceChatService {
    private static int maxId=0;
    private final static Set<Chat> chats = new HashSet<>();

    public PersistenceChatServiceImpl() {
    }


    @Override
    public Optional<Chat> getChat(int id) {
        return chats.stream().filter(chat -> chat.getId() == id).findFirst();
    }

    @Override
    public void removeChat(int id) {
        mockRemove(id);
    }

    @Override
    public Collection<Chat> get() {
        return mockGet();
    }


    @Override
    public void updateChat(Chat chat) throws ChatUpdateException {
        mockUpdateChat(chat);
    }

    @Override
    public void addChat(Chat chat) {
        mockAddChat(chat);
    }


    private void mockUpdateChat(Chat chat) throws ChatUpdateException {
        removeChat(chat.getId());
        addChat(chat);
    }

    private void mockAddChat(Chat chat) {
        chat.setId(maxId++);
        chats.add(chat);
    }


    private Collection<Chat> mockGet() {
        return chats;
    }

    private void mockRemove(int id) {
        Optional<Chat> chat = getChat(id);
        chat.ifPresent(chats::remove);
    }
}
