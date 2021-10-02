package chatApp.services.persistence.implementation;

import chatApp.domain.chat.Chat;
import chatApp.domain.exceptions.ChatUpdateException;
import chatApp.services.persistence.interfaces.PersistenceChatService;
import chatApp.services.persistence.interfaces.Repository;

import java.util.*;

public abstract class PersistenceChatServiceImpl<T extends Chat> implements PersistenceChatService<T> {
    private Repository<T> repository;
    private static int maxId=0;

    public PersistenceChatServiceImpl() {
    }

    public void setRepository(Repository<T> repository) {
        this.repository = this.repository;
    }

    @Override
    public  Optional<T> getChat(int id) {
        return repository.get().stream().filter(chat -> chat.getId() == id).findFirst();
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
    public void updateChat(T chat) throws ChatUpdateException {
        mockUpdateChat(chat);
    }

    @Override
    public void addChat(T chat) {
        mockAddChat(chat);
    }


    private void mockUpdateChat(T chat) throws ChatUpdateException {
        removeChat(chat.getId());
        addChat(chat);
    }

    private void mockAddChat(T chat) {
        chat.setId(maxId++);
        repository.add(chat);
    }


    private Collection<T> mockGet() {
        return repository.get();
    }

    private void mockRemove(int id) {
        Optional<T> chat = getChat(id);
        chat.ifPresent(repository::delete);
    }
}
