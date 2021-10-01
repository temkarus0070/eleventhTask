package chatApp.services.persistence.implementation;

import chatApp.domain.chat.Chat;
import chatApp.domain.exceptions.ChatUpdateException;
import chatApp.services.persistence.InMemoryStorage;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.lang.reflect.Field;
import java.util.*;

public abstract class PersistenceChatServiceImpl<T extends Chat> implements PersistenceChatService<T> {
    private InMemoryStorage inMemoryStorage;
    private static int maxId=0;
    private final static Set chats = new HashSet<T>();

    public PersistenceChatServiceImpl() {
    }

    public void setInMemoryStorage(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }

    @Override
    public Optional<T extends Chat> getChat(int id) {
        return inMemoryStorage.getChats().stream().filter(chat -> chat.getId() == id).findFirst();
    }

    @Override
    public void removeChat(int id) {
        mockRemove(id);
    }

    @Override
    public Collection<T> get() {
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
