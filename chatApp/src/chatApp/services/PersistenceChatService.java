package chatApp.services;

import chatApp.domain.chat.Chat;

public interface PersistenceChatService {
    public Chat getChat(int id);
    public void removeChat(int id);
    public void updateChat(Chat chat);
    public void addChat(Chat chat);
}
