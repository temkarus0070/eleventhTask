package org.temkarus0070.application.services.persistence.implementation;

import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.Message;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.domain.exceptions.UserBannedException;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;

public class PersistenceChatServiceImpl<T extends Chat> implements PersistenceChatService<T> {
    private final ChatRepository chatRepository;

    public PersistenceChatServiceImpl(ChatRepository repository) {
        this.chatRepository = repository;
    }


    public void removeChat(int id) throws ChatAppDatabaseException {
        chatRepository.delete(id);
    }

    @Override
    public Collection<T> get() throws ChatAppDatabaseException {
        return (Collection<T>) chatRepository.get();
    }


    public void updateChat(T chat) throws ChatAppDatabaseException {
        this.chatRepository.update(chat);
    }

    @Override
    public Collection<T> getChatsByUserName(String username) throws ChatAppDatabaseException {
        return (Collection<T>) chatRepository.getChatsByUser(username);
    }


    public void addChat(T chat) throws ChatAppDatabaseException {
        chatRepository.add(chat);
    }


    public void addUser(String username, int chatId) throws ChatAppDatabaseException {
        chatRepository.addUserToChat(username, chatId);
    }


    public void banUserInChat(String username, int chatId) throws ChatAppDatabaseException {
        chatRepository.banUserInChat(username, chatId);
    }


    public void removeUserFromChat(String username, int chatId) throws ChatAppDatabaseException {
        chatRepository.removeUserFromChat(username, chatId);
    }

    public Optional<T> getChat(int chatId) throws ChatAppDatabaseException {
        return (Optional<T>) Optional.of(chatRepository.get(chatId));
    }


    public void addMessage(Message message, int chatId) throws ChatAppDatabaseException {
        Optional<T> chat = getChat(chatId);
        if (chat.isPresent()) {
            if (chat.get().getBannedUsers().contains(message.getSender())) {
                MyLogger.log(Level.SEVERE, String.format("user %s was banned", message.getSender().getName()));
                throw new ChatAppDatabaseException(new UserBannedException());
            } else
                chatRepository.addMessage(message, chatId);
        }

    }
}