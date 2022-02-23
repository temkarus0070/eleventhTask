package org.temkarus0070.application.services;

import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.GroupChat;
import org.temkarus0070.application.domain.chat.PrivateChat;
import org.temkarus0070.application.domain.chat.RoomChat;

@Component
public class ChatConverterService {
    public <T extends Chat> T convert(GroupChat chat) throws ClassNotFoundException {

        switch (chat.getType()) {
            case PRIVATE:
                return (T) new PrivateChat();
            case ROOM:
                RoomChat roomChat = new RoomChat();
                roomChat.setName(chat.getName());
                roomChat.setChatOwner(chat.getChatOwner());
                return (T) roomChat;
            case GROUP:
                return (T) chat;
        }
        throw new ClassNotFoundException();
    }
}
