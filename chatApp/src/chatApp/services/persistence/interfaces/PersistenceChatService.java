package chatApp.services.persistence.interfaces;

import chatApp.domain.chat.Chat;
import chatApp.domain.exceptions.ChatUpdateException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PersistenceChatService {
    public Optional<Chat> getChat(int id);

    public void removeChat(int id);

    public Collection<Chat> get();

    public void updateChat(Chat chat) throws ChatUpdateException;

    public void addChat(Chat chat);
}
