package org.temkarus0070.application.facades;

import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.Message;
import org.temkarus0070.application.services.persistence.implementation.PersistenceChatServiceImpl;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

@Component
public class MessageFacade {
    private PersistenceChatServiceImpl<Chat> persistenceChatService;
    private PersistenceUserService persistenceUserService;
    private ChatRepository repository;
    private AuthFacade authFacade;

    public MessageFacade(PersistenceChatServiceImpl<Chat> persistenceChatService, PersistenceUserService persistenceUserService, ChatRepository repository, AuthFacade authFacade) {
        this.persistenceChatService = persistenceChatService;
        this.persistenceUserService = persistenceUserService;
        this.repository = repository;
        this.authFacade = authFacade;
    }


    public String add(Message message, int chatId) throws Exception {

        User currentUser = authFacade.getCurrentAppUser();
        message.setSender(currentUser);
        Chat chat = persistenceChatService.getChat(chatId).get();
        if (chat.getUserList().stream().anyMatch(e -> e.getUsername().equals(currentUser.getUsername()))) {
            persistenceChatService.addMessage(message, chatId);
            return currentUser.getUsername();
        } else throw new Exception("you cant write at this chat");
    }
}
