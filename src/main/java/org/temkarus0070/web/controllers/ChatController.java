package org.temkarus0070.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.exceptions.ChatAppException;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.persistence.implementation.PersistenceChatServiceImpl;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller("/chat")
public class ChatController {
    private final PersistenceChatServiceImpl<Chat> persistenceChatService;
    private final PersistenceUserService persistenceUserService;
    private final AuthService authService;

    public ChatController(PersistenceChatServiceImpl<Chat> persistenceChatService, PersistenceUserService persistenceUserService, AuthService authService) {
        this.persistenceChatService = persistenceChatService;
        this.persistenceUserService = persistenceUserService;
        this.authService = authService;
    }

    @GetMapping
    public String get(Model model, @RequestParam Integer chatId, HttpServletRequest req) {
        Optional<Chat> optionalChat = persistenceChatService.getChat(chatId);
        if (optionalChat.isPresent()) {
            try {


                Chat chat = optionalChat.get();
                User currentUser = authService.getCurrentUser(req.getCookies());
                if (hasPermissions(currentUser, chat)) {
                    Set<User> bannedUserSet = new HashSet<>(chat.getBannedUsers());
                    Set<User> currentUsers = new HashSet<>(chat.getUserList());

                    model.addAttribute("chat", chat);
                    model.addAttribute("users", persistenceUserService.getUsersNotAtThatChat(chat.getId()));
                    List<User> users = persistenceUserService.get().stream().filter(e -> !bannedUserSet.contains(e) && currentUsers.contains(e)).collect(Collectors.toList());
                    model.addAttribute("usersToBan", users);

                    return "chat";
                } else return "You dont have permissions to this chat";
            } catch (ChatAppException chatAppException) {
                return chatAppException.getMessage();
            }
        } else return "chat not found";
    }

    @PostMapping
    public String add(@RequestBody Chat chat, HttpServletRequest req, Model model) {
        try {
            User currentUser = authService.getCurrentUser(req.getCookies());
            persistenceChatService.addChat(chat);
            persistenceChatService.addUser(currentUser.getUsername(), chat.getId());
            model.addAttribute("chat", chat);
            model.addAttribute("chatType", chat.getType());
            model.addAttribute("id", chat.getId());
            return "chat";
        } catch (ChatAppException e) {
            return e.getMessage();
        }

    }

    @PostMapping("/addUser")
    public String addUser(Model model, HttpServletRequest req, @RequestBody User user, @RequestBody Chat chat) {
        try {
            User currentUser = authService.getCurrentUser(req.getCookies());
            chat = persistenceChatService.getChat(chat.getId()).get();
            if (chat == null) {
                return "chat not found";
            } else if (!chat.getUserList().contains(currentUser)) {
                return "you cant add user";
            } else {
                persistenceChatService.addUser(user.getUsername(), chat.getId());
                model.addAttribute("chat", chat);
                return "chat";
            }
        } catch (ChatAppException exception) {
            return exception.getMessage();
        }
    }

    @PostMapping("/ban")
    public String banUser(Model model, @RequestBody Chat chat, @RequestBody User user, HttpServletRequest req) {
        try {
            User currentUser = authService.getCurrentUser(req.getCookies());
            chat = persistenceChatService.getChat(chat.getId()).get();
            if (chat == null) {
                return "chat not found";
            } else if (!chat.getUserList().contains(currentUser)) {
                return "you cant ban user";
            } else {
                persistenceChatService.banUserInChat(user.getUsername(), chat.getId());
                model.addAttribute("chat", chat);
                return "chat";
            }
        } catch (ChatAppException exception) {
            return exception.getMessage();
        }
    }

    private boolean hasPermissions(User user, Chat chat) {
        return chat.getUserList().contains(user);
    }

}