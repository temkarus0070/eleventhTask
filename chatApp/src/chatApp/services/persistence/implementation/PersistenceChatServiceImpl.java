package chatApp.services.persistence.implementation;

import chatApp.domain.chat.Chat;
import chatApp.domain.exceptions.ChatUpdateException;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.lang.reflect.Field;
import java.util.*;

public class PersistenceChatServiceImpl implements PersistenceChatService {
    private final Set<Chat> chats = new HashSet<>();

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
        Field[] fields = chat.getClass().getFields();
        Optional<Chat> existedChat = getChat(chat.getId());
        if (existedChat.isPresent()) {
            Chat existedChatRef = existedChat.get();
            for (Field field : fields) {
                Class<? extends Chat> existedClassChat = existedChatRef.getClass();
                try {
                    Field usedField = existedClassChat.getDeclaredField(field.getName());
                    usedField.set(existedChatRef, field.get(chat));
                } catch (Exception ex) {
                    throw new ChatUpdateException();
                }
            }
        }
    }

    private void mockAddChat(Chat chat) {
        chats.add(chat);
    }


    private Collection<Chat> mockGet() {
        return chats;
    }

    private void mockRemove(int id) {
        Optional<Chat> chat = getChat(id);
        chat.ifPresent(value -> chats.remove(value));
    }
}
