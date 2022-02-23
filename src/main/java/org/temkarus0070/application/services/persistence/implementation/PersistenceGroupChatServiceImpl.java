package org.temkarus0070.application.services.persistence.implementation;

import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.chat.GroupChat;
import org.temkarus0070.application.domain.exceptions.ChatAlreadyExistsException;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.domain.exceptions.ChatUsersOverflowException;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PersistenceGroupChatServiceImpl extends PersistenceChatServiceImpl<GroupChat> implements PersistenceChatService<GroupChat> {
    private Logger myLogger = Logger.getLogger(this.getClass().getName());
    private final ChatRepository repository;

    public PersistenceGroupChatServiceImpl(ChatRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Optional<GroupChat> getChatByName(String name) throws ChatAppDatabaseException {
        return Optional.of((GroupChat) repository.getChatByName(name, ChatType.GROUP).get());

    }


    @Override
    public Optional<GroupChat> getChat(int id) throws ChatAppDatabaseException {
        try {
            return Optional.ofNullable((GroupChat) repository.get(id, ChatType.GROUP));
        } catch (ChatAppDatabaseException ex) {
            myLogger.log(Level.SEVERE, ex.getMessage());
            throw new ChatAppDatabaseException(ex.getMessage());
        }
    }

    @Override
    public Collection<GroupChat> get() throws ChatAppDatabaseException {
        return this.repository.get(ChatType.GROUP).stream()
                .map(chat -> (GroupChat) chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateChat(GroupChat chat) throws ChatAppDatabaseException {
        Optional<GroupChat> existedChat = getChat(chat.getId());
        if (existedChat.isPresent()) {
            if (chat.getUsersCount() < chat.getUserList().size()) {
                myLogger.log(Level.SEVERE, "Your chat size can't be less than current users count. Remove users or set greater value");
                throw new ChatAppDatabaseException("Your chat size can't be less than current users count. Remove users or set greater value");
            } else if (!chat.getName().equals(existedChat.get().getName())) {
                if (getChatByName(chat.getName()).isPresent()) {
                    ChatAlreadyExistsException chatAlreadyExistsException = new ChatAlreadyExistsException();
                    myLogger.log(Level.SEVERE, chatAlreadyExistsException.getMessage());
                    throw new ChatAppDatabaseException(chatAlreadyExistsException);
                }
            }
            this.repository.update(chat);
        }
    }


    @Override
    public void addChat(GroupChat chat) throws ChatAppDatabaseException {
        Optional<GroupChat> chatOptional = getChatByName(chat.getName());
        if (chatOptional.isPresent()) {
            ChatAlreadyExistsException chatAlreadyExistsException = new ChatAlreadyExistsException();
            myLogger.log(Level.SEVERE, chatAlreadyExistsException.getMessage());
            throw new ChatAppDatabaseException(chatAlreadyExistsException);
        } else {
            repository.add(chat);
        }
    }

    @Override
    public Collection<GroupChat> getChatsByUserName(String username) throws ChatAppDatabaseException {
        return this.repository.getChatsByUser(username, ChatType.GROUP).stream()
                .map(chat -> (GroupChat) chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void addUser(String username, int chatId) throws ChatAppDatabaseException {
        Optional<GroupChat> groupChat = getChat(chatId);
        if (groupChat.isPresent()) {
            if (groupChat.get().getUsersCount() >= groupChat.get().getUserList().size() + 1)
                repository.addUserToChat(username, chatId);
            else {
                ChatUsersOverflowException chatUsersOverflowException = new ChatUsersOverflowException();
                myLogger.log(Level.SEVERE, chatUsersOverflowException.getMessage());
                throw new ChatAppDatabaseException(chatUsersOverflowException);
            }
        }
    }
}
