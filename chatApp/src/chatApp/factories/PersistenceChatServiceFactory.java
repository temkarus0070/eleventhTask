package chatApp.factories;

import chatApp.domain.chat.ChatType;
import chatApp.services.chat.ChatService;
import chatApp.services.chat.GroupChatServiceImpl;
import chatApp.services.chat.PrivateChatServiceImpl;
import chatApp.services.chat.RoomCharServiceImpl;
import chatApp.services.persistence.implementation.PersistenceGroupChatServiceImpl;
import chatApp.services.persistence.implementation.PersistencePrivateChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceRoomChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceChatService;

public class PersistenceChatServiceFactory {
    public static PersistenceChatService create(ChatType chatType) throws ClassNotFoundException {
        switch (chatType){
            case PRIVATE -> {
                return new PersistencePrivateChatServiceImpl();
            }
            case ROOM -> {
                return new PersistenceRoomChatServiceImpl();
            }
            case GROUP -> {
                return new PersistenceGroupChatServiceImpl();
            }
            default -> {
                throw new ClassNotFoundException();
            }
        }
    }
}
