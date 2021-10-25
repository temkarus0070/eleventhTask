package chatApp.services.persistence.implementation;

import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.PrivateChat;
import chatApp.domain.exceptions.ChatUsersOverflowException;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersistencePrivateChatServiceImpl extends PersistenceChatServiceImpl<PrivateChat>  implements PersistenceChatService<PrivateChat>{
    private ChatRepository chatRepository;

    public PersistencePrivateChatServiceImpl(ChatRepository repository) {
        super(repository);
        this.chatRepository = repository;
    }

    @Override
    public Optional<PrivateChat> getChat(int id)throws Exception {
        return Stream.of(chatRepository.get(id))
                .filter(chat -> chat.getId() == id && chat.getType() == ChatType.PRIVATE)
                .map(chat -> (PrivateChat) chat)
                .findFirst();
    }



    @Override
    public Collection<PrivateChat> get()throws Exception {
        return chatRepository.get().stream()
                .filter(chat -> chat.getType() == ChatType.PRIVATE)
                .map(chat -> (PrivateChat) chat)
                .collect(Collectors.toSet());
    }



    @Override
    public Collection<PrivateChat> getChatsByUserName(String username)throws Exception {
        return this.chatRepository.getChatsByUser(username).stream().filter(chat->chat.getType()==ChatType.PRIVATE)
                .map(chat->(PrivateChat)chat)
                .collect(Collectors.toSet());
    }



    @Override
    public void addUser(String username, int chatId) throws Exception{
        Optional<PrivateChat> privateChat=getChat(chatId);
        if(privateChat.isPresent()) {
            if(privateChat.get().getUserList().size()<2)
                chatRepository.addUserToChat(username, chatId);
            else
                throw new ChatUsersOverflowException();
        }
    }

}
