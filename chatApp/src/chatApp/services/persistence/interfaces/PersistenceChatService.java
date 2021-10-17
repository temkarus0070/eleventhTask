package chatApp.services.persistence.interfaces;

import chatApp.domain.chat.Chat;
import chatApp.domain.exceptions.ChatAlreadyExistsException;

import java.util.Collection;
import java.util.Optional;

public interface PersistenceChatService<T extends Chat> {

    public  Optional<T> getChat(int id)throws Exception;

    public void removeChat(int id)throws Exception;

    public  Collection<T> get()throws Exception;

    public  void updateChat(T chat)throws Exception;

    public Collection<T> getChatsByName(String username)throws Exception;

    public  void addChat(T chat)throws ChatAlreadyExistsException, Exception;
}
