package org.temkarus0070.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.Message;
import org.temkarus0070.application.domain.exceptions.ChatAppException;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.persistence.implementation.PersistenceChatServiceImpl;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/messages")
public class MessagesController {
    private PersistenceChatServiceImpl<Chat> persistenceChatService;
    private PersistenceUserService persistenceUserService;
    private ChatRepository repository;
    private AuthService authService;

    public MessagesController(PersistenceChatServiceImpl<Chat> persistenceChatService, PersistenceUserService persistenceUserService, ChatRepository repository, AuthService authService) {
        this.persistenceChatService = persistenceChatService;
        this.persistenceUserService = persistenceUserService;
        this.repository = repository;
        this.authService = authService;
    }

    @PostMapping
    public String add(Message message, Model model, HttpServletRequest req, @RequestParam Integer chatId) {
        try {

            User currentUser = authService.getCurrentUser(req.getCookies());
            message.setSender(currentUser);
            persistenceChatService.addMessage(message, chatId);
            model.addAttribute("chat", persistenceChatService.getChat(chatId));
            return "chat";
        } catch (ChatAppException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
