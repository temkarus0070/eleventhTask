package chatApp.services.persistence.interfaces;

import chatApp.domain.chat.Chat;
import chatApp.domain.exceptions.ChatUpdateException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PersistenceChatService<T extends Chat> {

    public Optional<T> getChat(int id);

    public void removeChat(int id);

    public Collection<T> get();

    public void updateChat(T chat) throws ChatUpdateException;

    public void addChat(T chat);
}
