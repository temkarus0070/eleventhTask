package org.temkarus0070.application.facades;

import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.chat.GroupChat;
import org.temkarus0070.application.factories.PersistenceChatServiceFactory;
import org.temkarus0070.application.services.ChatConverterService;
import org.temkarus0070.application.services.persistence.implementation.PersistenceChatServiceImpl;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;

@Component
public class ChatFacade {
    private final PersistenceChatServiceImpl<Chat> persistenceChatService;
    private final PersistenceChatServiceFactory persistenceChatServiceFactory;
    private final ChatConverterService chatConverterService;
    private final ChatRepository chatRepository;
    private final AuthFacade authFacade;

    public ChatFacade(PersistenceChatServiceImpl<Chat> persistenceChatService, PersistenceChatServiceFactory persistenceChatServiceFactory, ChatConverterService chatConverterService, ChatRepository chatRepository, AuthFacade authFacade) {
        this.persistenceChatService = persistenceChatService;
        this.persistenceChatServiceFactory = persistenceChatServiceFactory;
        this.chatConverterService = chatConverterService;
        this.chatRepository = chatRepository;
        this.authFacade = authFacade;
    }

    public Chat get(Integer chatId, String chatName, ChatType chatType) throws Throwable {
        User currentUser = authFacade.getCurrentAppUser();
        Optional<Chat> optionalChat = Optional.empty();
        PersistenceChatService persistenceChatService = persistenceChatServiceFactory.create(chatType, chatRepository);
        if (chatId == null)
            optionalChat = persistenceChatService.getChatByName(chatName);
        else optionalChat = persistenceChatService.getChat(chatId);
        Chat chat = optionalChat.orElseThrow(() -> new Exception("chat not found"));
        if (hasPermissions(currentUser, chat) || currentUser.getRoles().contains("ADMIN")) {
            return chat;
        } else
            throw new Exception("You dont have permissions to this chat");
    }

    public int add(GroupChat anyChat, ChatType type) throws ClassNotFoundException {
        User currentUser = authFacade.getCurrentAppUser();
        anyChat.setType(type);
        Chat chat = chatConverterService.convert(anyChat);
        chat.setChatOwner(currentUser);
        persistenceChatService.addChat(chat);
        return chat.getId();
    }

    public void addUser(User user, Integer chatId) throws Exception {
        User currentUser = authFacade.getCurrentAppUser();
        Chat chat = persistenceChatService.getChat(chatId).orElseThrow(() -> new Exception("chat not found"));
        if (!chat.getUserList().contains(currentUser)) {
            throw new Exception("you cant add user");
        } else {
            persistenceChatService.addUser(user.getUsername(), chat.getId());
        }

    }

    public Collection<Chat> getAllChatsOfCurrentUser(ChatType chatType) throws ClassNotFoundException {
        User currentUser = authFacade.getCurrentAppUser();
        if (chatType == null) {
            chatType = ChatType.ANY;
        }
        PersistenceChatService persistenceChatService = persistenceChatServiceFactory.create(chatType, chatRepository);
        if (currentUser.getRoles().contains("ADMIN")) {
            return persistenceChatService.get();
        } else
            return persistenceChatService.getChatsByUserName(currentUser.getUsername());
    }

    public void banUser(Integer chatId, User user) throws Exception {
        User currentUser = authFacade.getCurrentAppUser();
        Chat chat = persistenceChatService.getChat(chatId).orElseThrow(() -> new Exception("chat not found"));
        if (!chat.getUserList().contains(currentUser)) {
            throw new Exception("you cant ban user");
        } else {
            persistenceChatService.banUserInChat(user.getUsername(), chat.getId());
        }
    }


    private boolean hasPermissions(User user, Chat chat) {
        return chat.getUserList().contains(user);
    }


}
