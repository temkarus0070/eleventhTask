package org.temkarus0070.application.services.persistence.implementation;

import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.chat.RoomChat;
import org.temkarus0070.application.domain.exceptions.ChatAlreadyExistsException;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PersistenceRoomChatServiceImpl extends PersistenceChatServiceImpl<RoomChat>  implements PersistenceChatService<RoomChat>{
    private Logger myLogger = Logger.getLogger(this.getClass().getName());
    private final ChatRepository chatRepository;

    public PersistenceRoomChatServiceImpl(ChatRepository repository) {
        super(repository);
        this.chatRepository = repository;
    }

    public Optional<RoomChat> getChatByName(String name) throws ChatAppDatabaseException {
        return Optional.of((RoomChat) chatRepository.getChatByName(name, ChatType.ROOM).get());
    }


    @Override
    public Optional<RoomChat> getChat(int id) throws ChatAppDatabaseException {
        try {
            return Optional.ofNullable((RoomChat) chatRepository.get(id, ChatType.ROOM));
        } catch (ChatAppDatabaseException ex) {
            myLogger.log(Level.SEVERE, String.format("chat not found with id=%d", id));
            throw new ChatAppDatabaseException("chat not found ChatAppDatabaseException");
        }
    }


    @Override
    public Collection<RoomChat> get() throws ChatAppDatabaseException {
        return this.chatRepository.get(ChatType.ROOM).stream()
                .map(chat -> (RoomChat) chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateChat(RoomChat chat) throws ChatAppDatabaseException {
        Optional<RoomChat> existedChat = getChat(chat.getId());
        if (existedChat.isPresent()) {
            if (!chat.getName().equals(existedChat.get().getName())) {
                if (getChatByName(chat.getName()).isPresent()) {
                    ChatAlreadyExistsException chatAlreadyExistsException = new ChatAlreadyExistsException();
                    myLogger.log(Level.SEVERE, chatAlreadyExistsException.getMessage());
                    throw new ChatAppDatabaseException(chatAlreadyExistsException);
                }
            }
            this.chatRepository.update(chat);
        }
    }

    @Override
    public Collection<RoomChat> getChatsByUserName(String username) throws ChatAppDatabaseException {
        return this.chatRepository.getChatsByUser(username, ChatType.ROOM).stream()
                .map(chat -> (RoomChat) chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void addChat(RoomChat chat) throws ChatAppDatabaseException {
        Optional<RoomChat> chatOptional = getChatByName(chat.getName());
        if (chatOptional.isPresent()) {
            ChatAlreadyExistsException chatAlreadyExistsException = new ChatAlreadyExistsException();
            myLogger.log(Level.SEVERE, chatAlreadyExistsException.getMessage());
            throw new ChatAppDatabaseException(chatAlreadyExistsException);
        } else {
            chatRepository.add(chat);
        }
    }
}
