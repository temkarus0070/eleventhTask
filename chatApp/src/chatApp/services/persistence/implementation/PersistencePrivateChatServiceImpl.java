package chatApp.services.persistence.implementation;

import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.PrivateChat;
import chatApp.domain.exceptions.ChatAppDatabaseException;
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
    public Optional<PrivateChat> getChat(int id) throws ChatAppDatabaseException {
        try {
            return Stream.of(chatRepository.get(id))
                    .map(chat -> (PrivateChat) chat)
                    .findFirst();
        } catch (ChatAppDatabaseException ex) {
            throw new ChatAppDatabaseException("chat not found ChatAppDatabaseException");
        }
    }


    @Override
    public Collection<PrivateChat> get() throws ChatAppDatabaseException {
        return chatRepository.get().stream()
                .map(chat -> (PrivateChat) chat)
                .collect(Collectors.toSet());
    }


    @Override
    public Collection<PrivateChat> getChatsByUserName(String username) throws ChatAppDatabaseException {
        return this.chatRepository.getChatsByUser(username).stream()
                .map(chat -> (PrivateChat) chat)
                .collect(Collectors.toSet());
    }


    @Override
    public void addUser(String username, int chatId) throws ChatAppDatabaseException {
        Optional<PrivateChat> privateChat = getChat(chatId);
        if (privateChat.isPresent()) {
            if (privateChat.get().getUserList().size() < 2)
                chatRepository.addUserToChat(username, chatId);
            else
                throw new ChatAppDatabaseException(new ChatUsersOverflowException());
        }
    }

}
