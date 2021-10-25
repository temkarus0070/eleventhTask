package chatApp.services.persistence.interfaces;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.Message;
import chatApp.domain.chat.PrivateChat;
import chatApp.domain.exceptions.ChatAlreadyExistsException;

import java.util.Collection;
import java.util.Optional;

public interface PersistenceChatService<T extends Chat> {

    public  Optional<T> getChat(int id)throws Exception;

    public void removeChat(int id)throws Exception;

    public  Collection<T> get()throws Exception;

    public  void updateChat(T chat)throws Exception;

    public Collection<T> getChatsByUserName(String username)throws Exception;

    public  void addChat(T chat)throws ChatAlreadyExistsException, Exception;

    public void addUser(String username,int chatId) throws Exception;

    public void banUserInChat(String username,int chatId) throws Exception;

    public void removeUserFromChat(String username,int chatId) throws Exception;

    public void addMessage(Message message,int chatId)throws Exception;
}
