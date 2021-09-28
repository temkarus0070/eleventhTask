package chatApp.services;

import chatApp.domain.chat.Chat;
import chatApp.services.persistence.implementation.PersistenceGroupChatServiceImpl;
import chatApp.services.persistence.implementation.PersistencePrivateChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceRoomChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Map;
import java.util.Optional;

public class PersistenceChatServiceSelector {
    public PersistenceChatService getPersistenceChatService(Map<String, String[]> parameterMap) throws ClassNotFoundException {
            String type = parameterMap.get("chatType")[0];
            Optional<Chat> chat;
            switch (type) {
                case "PrivateChat":
                    return new PersistencePrivateChatServiceImpl();
                case "GroupChat":
                    return new PersistenceGroupChatServiceImpl();
                case "RoomChat":
                    return new PersistenceRoomChatServiceImpl();
            }
            throw new ClassNotFoundException();
    }
}
