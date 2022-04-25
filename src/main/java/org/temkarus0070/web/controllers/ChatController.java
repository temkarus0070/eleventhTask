package org.temkarus0070.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller()
@RequestMapping("/chat")
public class ChatController {
    private PersistenceChatServiceFactory persistenceChatServiceFactory;
    private ChatConverterService chatConverterService;
    private final PersistenceChatServiceImpl<Chat> persistenceChatService;
    private final PersistenceUserService persistenceUserService;
    private final AuthService authService;
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
    public String get(Model model, @RequestParam(required = false) Integer chatId, HttpServletRequest req, @RequestParam(required = false) String chatName, @RequestParam ChatType chatType, Principal principal, Authentication authentication) {
        Optional optionalChat = Optional.empty();
        try {

            if (chatType == ChatType.GROUP) {
                PersistenceGroupChatServiceImpl persistenceChatService = (PersistenceGroupChatServiceImpl) persistenceChatServiceFactory.create(chatType, chatRepository);
                if (chatId == null)
                    optionalChat = persistenceChatService.getChatByName(chatName);
                else optionalChat = persistenceChatService.getChat(chatId);
            } else if (chatType == ChatType.ROOM) {
                PersistenceRoomChatServiceImpl persistenceChatService = (PersistenceRoomChatServiceImpl) persistenceChatServiceFactory.create(chatType, chatRepository);
              if (chatId==null)
                  optionalChat = persistenceChatService.getChatByName(chatName);
              else optionalChat=persistenceChatService.getChat(chatId);
          } else {
                PersistenceChatService persistenceChatService = persistenceChatServiceFactory.create(chatType, chatRepository);
              optionalChat = persistenceChatService.getChat(chatId);
          }

          if (optionalChat.isPresent()) {
              Chat chat = (Chat) optionalChat.get();
              User currentUser = new User(principal.getName());
              if (hasPermissions(currentUser, chat) || authentication.getAuthorities().stream().anyMatch(e -> e.getAuthority().contains("ADMIN"))) {
                  Set<User> bannedUserSet = new HashSet<>(chat.getBannedUsers());
                  Set<User> currentUsers = new HashSet<>(chat.getUserList());

                  model.addAttribute("chat", chat);
                  model.addAttribute("users", persistenceUserService.getUsersNotAtThatChat(chat.getId()));
                  List<User> users = persistenceUserService.get().stream().filter(e -> !bannedUserSet.contains(e) && currentUsers.contains(e)).collect(Collectors.toList());
                  model.addAttribute("usersToBan", users);

                  return "chat";
              } else return "You dont have permissions to this chat";

          } else  model.addAttribute("error", "chat not found");
      }
      catch (Exception exception){
          model.addAttribute("error", exception.getMessage());
      }
        return "error";
    }


    @PostMapping
    public String add(GroupChat anyChat, HttpServletRequest req, Model model, ChatType type, Principal principal) {
        try {
            anyChat.setType(type);
            Chat chat = chatConverterService.convert(anyChat);
            User currentUser = new User(principal.getName());
            chat.setChatOwner(currentUser);
            persistenceChatService.addChat(chat);
            model.addAttribute("chat", chat);
            model.addAttribute("chatType", chat.getType());
            model.addAttribute("id", chat.getId());
            return "redirect:/chat?chatId=" + chat.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

    }


    @PostMapping("/addUser")
    public String addUser(Model model, HttpServletRequest req, User user, Integer id, Principal principal) {
        User currentUser = new User(principal.getName());
        Chat chat = persistenceChatService.getChat(id).get();
        if (chat == null) {
            return "chat not found";
        } else if (!chat.getUserList().contains(currentUser)) {
            return "you cant add user";
        } else {
            persistenceChatService.addUser(user.getUsername(), chat.getId());
            model.addAttribute("chat", chat);
            return "redirect:/chat?chatId=" + chat.getId();
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
            return "redirect:/chat?chatId=" + chat.getId();
        }
    }

    private boolean hasPermissions(User user, Chat chat) {
        return chat.getUserList().contains(user);
    }

}
