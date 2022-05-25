package org.temkarus0070.web.RestControllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.Message;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.persistence.implementation.PersistenceChatServiceImpl;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/messages")
public class MessagesRestController {
    private PersistenceChatServiceImpl<Chat> persistenceChatService;
    private PersistenceUserService persistenceUserService;
    private ChatRepository repository;
    private AuthService authService;

    public MessagesRestController(PersistenceChatServiceImpl<Chat> persistenceChatService, PersistenceUserService persistenceUserService, ChatRepository repository, AuthService authService) {
        this.persistenceChatService = persistenceChatService;
        this.persistenceUserService = persistenceUserService;
        this.repository = repository;
        this.authService = authService;
    }

    @PostMapping
    public String add(@RequestBody Message message, @RequestParam int chatId, Principal principal) {
        User currentUser = new User(principal.getName());
        message.setSender(currentUser);
        Chat chat = persistenceChatService.getChat(chatId).get();
        if (chat.getUserList().stream().anyMatch(e -> e.getUsername().equals(principal.getName()))) {
            persistenceChatService.addMessage(message, chatId);
            return currentUser.getUsername();
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you cant write at this chat");
    }
}
