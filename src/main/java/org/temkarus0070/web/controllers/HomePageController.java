package org.temkarus0070.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.factories.PersistenceChatServiceFactory;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceChatService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Controller
public class HomePageController {

    private AuthService authService;
    private PersistenceChatServiceFactory persistenceChatServiceFactory;
    private ChatRepository chatRepository;

    public HomePageController(AuthService authService, PersistenceChatServiceFactory persistenceChatServiceFactory, ChatRepository chatRepository) {
        this.authService = authService;
        this.persistenceChatServiceFactory = persistenceChatServiceFactory;
        this.chatRepository = chatRepository;
    }

    @GetMapping("/index")
    public String home(Model model, HttpServletRequest req, @RequestParam(required = false) ChatType chatType, HttpServletResponse resp, Principal principal, Authentication authentication) throws IOException {
        try {
            User currentUser = new User(principal.getName());
            if (chatType == null) {
                chatType = ChatType.ANY;
            }
            PersistenceChatService persistenceChatService = persistenceChatServiceFactory.create(chatType, chatRepository);
            if (authentication.getAuthorities().stream().anyMatch(e -> e.getAuthority().contains("ADMIN"))) {
                model.addAttribute("chats", persistenceChatService.get());
            } else
                model.addAttribute("chats", persistenceChatService.getChatsByUserName(currentUser.getUsername()));
            model.addAttribute("chatType", chatType);

        } catch (Exception exception) {
            model.addAttribute("error", exception.getMessage());
            return "redirect:/login";
        }
        return "index";
    }
}
