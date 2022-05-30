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
    private UserFacade userFacade;

    public MessageFacade(PersistenceChatServiceImpl<Chat> persistenceChatService, PersistenceUserService persistenceUserService, ChatRepository repository, UserFacade userFacade) {
        this.persistenceChatService = persistenceChatService;
        this.persistenceUserService = persistenceUserService;
        this.repository = repository;
        this.userFacade = userFacade;
    }


    public String add(Message message, int chatId) throws Exception {

        User currentUser = userFacade.getCurrentAppUser();
        message.setSender(currentUser);
        Chat chat = persistenceChatService.getChat(chatId).get();
        if (chat.getUserList().stream().anyMatch(e -> e.getUsername().equals(currentUser.getUsername()))) {
            persistenceChatService.addMessage(message, chatId);
            return currentUser.getUsername();
        } else throw new Exception("you cant write at this chat");
    }
}
