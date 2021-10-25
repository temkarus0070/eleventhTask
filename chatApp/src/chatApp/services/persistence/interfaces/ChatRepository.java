package chatApp.services.persistence.interfaces;

import chatApp.domain.chat.Chat;
import chatApp.domain.chat.Message;

import java.util.Collection;
import java.util.Optional;

public interface ChatRepository extends Repository<Chat,Integer> {
    public Collection<Chat> getChatsByUser(String username)throws Exception;
    public Optional<Chat> getChatByName(String name)throws Exception;
    public void removeUserFromChat(String user,Integer chatId)throws Exception;
    public void addUserToChat(String user,Integer chatId) throws Exception;
    public void banUserInChat(String user,Integer chatId) throws Exception;
    public void addMessage(Message message,Integer chatId) throws Exception;
}
