package chatApp.services.persistence.implementation;

import chatApp.domain.chat.Chat;
import chatApp.domain.chat.Message;
import chatApp.domain.chat.PrivateChat;
import chatApp.domain.exceptions.ChatAlreadyExistsException;
import chatApp.domain.exceptions.UserBannedException;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;

public class PersistenceChatServiceImpl<T extends Chat> implements PersistenceChatService<T> {
    private ChatRepository chatRepository;

    public PersistenceChatServiceImpl(ChatRepository repository) {
        this.chatRepository = repository;
    }




    public void removeChat(int id) throws Exception {
        chatRepository.delete(id);
    }

    @Override
    public Collection<T> get() throws Exception {
        throw new UnsupportedOperationException();
    }


    public void updateChat(T chat) throws Exception {
        this.chatRepository.update(chat);
    }

    @Override
    public Collection<T> getChatsByUserName(String username) throws Exception {
        throw new UnsupportedOperationException();
    }


    public void addChat(T chat) throws Exception {
        chatRepository.add(chat);
    }


    public void addUser(String username, int chatId) throws Exception {
        chatRepository.addUserToChat(username,chatId);
    }


    public void banUserInChat(String username, int chatId) throws Exception {
        chatRepository.banUserInChat(username,chatId);
    }


    public void removeUserFromChat(String username, int chatId) throws Exception {
        chatRepository.removeUserFromChat(username,chatId);
    }

    public Optional<T> getChat(int chatId) throws Exception{
        return (Optional<T>) Optional.of(chatRepository.get(chatId));
    }




    public void addMessage(Message message,int chatId)throws Exception{
        Optional<T> chat=getChat(chatId);
        if(chat.isPresent()){
            if(chat.get().getBannedUsers().contains(message.getSender())){
                throw new UserBannedException();
            }
            else
                chatRepository.addMessage(message,chatId);
        }

    }
}
