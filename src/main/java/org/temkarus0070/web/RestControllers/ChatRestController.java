package org.temkarus0070.web.RestControllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.chat.GroupChat;
import org.temkarus0070.application.facades.ChatFacade;

import java.util.Collection;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {
    private ChatFacade chatFacade;

    public ChatRestController(ChatFacade chatFacade) {
        this.chatFacade = chatFacade;
    }

    //сократим
    @GetMapping()
    public Chat get(@RequestParam(required = false) Integer chatId, @RequestParam(required = false) String chatName, @RequestParam ChatType chatType) throws Throwable {
        try {
            return chatFacade.get(chatId, chatName, chatType);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/currentUser")
    public Collection<Chat> getAllChatsOfCurrentUser(@RequestParam(required = false) ChatType chatType) {
        try {
            return chatFacade.getAllChatsOfCurrentUser(chatType);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }


    @PostMapping()
    public int add(@RequestBody GroupChat anyChat, @RequestParam ChatType type) {
        try {
            return chatFacade.add(anyChat, type);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @PostMapping("/newUser")
    public void addUser(@RequestBody User user, @RequestParam Integer chatId) {
        try {
            chatFacade.banUser(chatId, user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @PostMapping("/ban")
    public void banUser(@RequestParam Integer chatId, @RequestBody User user) {
        try {
            chatFacade.banUser(chatId, user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
