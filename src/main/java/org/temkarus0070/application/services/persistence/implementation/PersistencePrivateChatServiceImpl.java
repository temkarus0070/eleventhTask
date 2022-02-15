package org.temkarus0070.application.services.persistence.implementation;

import org.temkarus0070.application.domain.chat.PrivateChat;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.domain.exceptions.ChatUsersOverflowException;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PersistencePrivateChatServiceImpl extends PersistenceChatServiceImpl<PrivateChat>  implements PersistenceChatService<PrivateChat>{
    private ChatRepository chatRepository;

    public PersistencePrivateChatServiceImpl(ChatRepository repository) {
        super(repository);
        this.chatRepository = repository;
    }

    @Override
    public Optional<PrivateChat> getChat(int id) throws ChatAppDatabaseException {
        try {
            return Optional.ofNullable((PrivateChat) chatRepository.get(id));
        } catch (ChatAppDatabaseException ex) {
            MyLogger.log(Level.SEVERE, ex.getMessage());
            throw new ChatAppDatabaseException(ex.getMessage());
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
            else {
                ChatUsersOverflowException chatUsersOverflowException = new ChatUsersOverflowException();
                MyLogger.log(Level.SEVERE, chatUsersOverflowException.getMessage());
                throw new ChatAppDatabaseException(chatUsersOverflowException);
            }
        }
    }

}
