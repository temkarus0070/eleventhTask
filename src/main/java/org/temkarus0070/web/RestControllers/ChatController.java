package org.temkarus0070.web.RestControllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.chat.GroupChat;
import org.temkarus0070.application.factories.PersistenceChatServiceFactory;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.ChatConverterService;
import org.temkarus0070.application.services.persistence.implementation.PersistenceChatServiceImpl;
import org.temkarus0070.application.services.persistence.implementation.PersistenceGroupChatServiceImpl;
import org.temkarus0070.application.services.persistence.implementation.PersistenceRoomChatServiceImpl;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceChatService;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final PersistenceChatServiceImpl<Chat> persistenceChatService;
    private final PersistenceUserService persistenceUserService;
    private final AuthService authService;
    private PersistenceChatServiceFactory persistenceChatServiceFactory;
    private ChatConverterService chatConverterService;
    private ChatRepository chatRepository;

    public ChatController(PersistenceChatServiceFactory persistenceChatServiceFactory, ChatConverterService chatConverterService, PersistenceChatServiceImpl<Chat> persistenceChatService, PersistenceUserService persistenceUserService, AuthService authService, ChatRepository chatRepository) {
        this.persistenceChatServiceFactory = persistenceChatServiceFactory;
        this.chatConverterService = chatConverterService;
        this.persistenceChatService = persistenceChatService;
        this.persistenceUserService = persistenceUserService;
        this.authService = authService;
        this.chatRepository = chatRepository;
    }

    @GetMapping
    public Chat get(@RequestParam(required = false) Integer chatId, @RequestParam(required = false) String chatName, @RequestParam ChatType chatType, Principal principal, Authentication authentication) {
        Optional optionalChat = Optional.empty();
        try {

            if (chatType == ChatType.GROUP) {
                PersistenceGroupChatServiceImpl persistenceChatService = (PersistenceGroupChatServiceImpl) persistenceChatServiceFactory.create(chatType, chatRepository);
                if (chatId == null)
                    optionalChat = persistenceChatService.getChatByName(chatName);
                else optionalChat = persistenceChatService.getChat(chatId);
            } else if (chatType == ChatType.ROOM) {
                PersistenceRoomChatServiceImpl persistenceChatService = (PersistenceRoomChatServiceImpl) persistenceChatServiceFactory.create(chatType, chatRepository);
                if (chatId == null)
                    optionalChat = persistenceChatService.getChatByName(chatName);
                else optionalChat = persistenceChatService.getChat(chatId);
            } else {
                PersistenceChatService persistenceChatService = persistenceChatServiceFactory.create(chatType, chatRepository);
                optionalChat = persistenceChatService.getChat(chatId);
            }

            if (optionalChat.isPresent()) {
                Chat chat = (Chat) optionalChat.get();
                User currentUser = new User(principal.getName());
                if (hasPermissions(currentUser, chat) || authentication.getAuthorities().stream().anyMatch(e -> e.getAuthority().contains("ADMIN"))) {
                    return chat;
                } else
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permissions to this chat");

            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "chat not found");
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }


    @PostMapping
    public void add(GroupChat anyChat, ChatType type, Principal principal) {
        try {
            anyChat.setType(type);
            Chat chat = chatConverterService.convert(anyChat);
            User currentUser = new User(principal.getName());
            chat.setChatOwner(currentUser);
            persistenceChatService.addChat(chat);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PostMapping("/addUser")
    public void addUser(User user, Integer id, Principal principal) {
        User currentUser = new User(principal.getName());
        Chat chat = persistenceChatService.getChat(id).get();
        if (chat == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "chat not found");
        } else if (!chat.getUserList().contains(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you cant add user");
        } else {
            persistenceChatService.addUser(user.getUsername(), chat.getId());
        }

    }

    @PostMapping("/ban")
    public String banUser(Model model, Integer id, User user, HttpServletRequest req, Principal principal) {
        User currentUser = new User(principal.getName());
        Chat chat = persistenceChatService.getChat(id).get();
        if (chat == null) {
            return "chat not found";
        } else if (!chat.getUserList().contains(currentUser)) {
            return "you cant ban user";
        } else {
            persistenceChatService.banUserInChat(user.getUsername(), chat.getId());
            model.addAttribute("chat", chat);
            return "redirect:/chat?chatId=" + chat.getId() + "&&chatType=" + chat.getType();
        }
    }

    private boolean hasPermissions(User user, Chat chat) {
        return chat.getUserList().contains(user);
    }
}
