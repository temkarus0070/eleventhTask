package org.temkarus0070.web.RestControllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.temkarus0070.application.domain.chat.Message;
import org.temkarus0070.application.facades.MessageFacade;

@RestController
@RequestMapping("/api/messages")
public class MessagesRestController {
    private MessageFacade messageFacade;

    public MessagesRestController(MessageFacade messageFacade) {
        this.messageFacade = messageFacade;
    }

    @PostMapping
    public String add(@RequestBody Message message, @RequestParam int chatId) {
        try {
            return messageFacade.add(message, chatId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
