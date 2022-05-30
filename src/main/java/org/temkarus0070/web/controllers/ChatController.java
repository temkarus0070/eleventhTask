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
import org.temkarus0070.application.facades.ChatFacade;
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
  private ChatFacade chatFacade;

    @GetMapping
    public String get(Model model, @RequestParam(required = false) Integer chatId, HttpServletRequest req, @RequestParam(required = false) String chatName, @RequestParam ChatType chatType, Principal principal, Authentication authentication) {
        Optional optionalChat = Optional.empty();
        try {
            Chat chat1 = null;
            try {
                chat1 = chatFacade.get(chatId, chatName, chatType);
            } catch (Throwable e) {
                model.addAttribute("error", e.getMessage());
            }
            chatFacade.fillModelOfChat(model,chat1);
            return "chat";
      }
      catch (Exception exception){
          model.addAttribute("error", exception.getMessage());
      }
        return "error";
    }


    @PostMapping
    public String add(GroupChat anyChat, HttpServletRequest req, Model model, ChatType type, Principal principal) {
        try {
            chatFacade.add(anyChat,type);
            model.addAttribute("chat", anyChat);
            model.addAttribute("chatType", anyChat.getType());
            model.addAttribute("id", anyChat.getId());
            return "redirect:/chat?chatId=" + anyChat.getId();
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
            return "redirect:/chat?chatId=" + chat.getId() + "&&chatType=" + chat.getType();
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
