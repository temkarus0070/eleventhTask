package chatApp.services.persistence.interfaces;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.Message;
import chatApp.domain.chat.PrivateChat;
import chatApp.domain.exceptions.ChatAppDatabaseException;

import java.util.Collection;
import java.util.Optional;

public interface PersistenceChatService<T extends Chat> {

    public Optional<T> getChat(int id) throws ChatAppDatabaseException;

    public void removeChat(int id) throws ChatAppDatabaseException;

    public Collection<T> get() throws ChatAppDatabaseException;

    public void updateChat(T chat) throws ChatAppDatabaseException;

    public Collection<T> getChatsByUserName(String username) throws ChatAppDatabaseException;

    public void addChat(T chat) throws ChatAppDatabaseException;

    public void addUser(String username, int chatId) throws ChatAppDatabaseException;

    public void banUserInChat(String username, int chatId) throws ChatAppDatabaseException;

    public void removeUserFromChat(String username, int chatId) throws ChatAppDatabaseException;

    public void addMessage(Message message, int chatId) throws ChatAppDatabaseException;
}
