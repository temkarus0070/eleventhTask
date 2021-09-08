package chatApp.services.persistence;

import chatApp.domain.chat.Chat;

import java.util.Optional;

public interface PersistenceChatService {
    public Optional<Chat> getChat(int id);
    public void removeChat(int id);
    public void updateChat(Chat chat);
    public void addChat(Chat chat);
}
