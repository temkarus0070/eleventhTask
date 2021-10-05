package chatApp.factories;

import chatApp.domain.chat.ChatType;
import chatApp.services.chat.ChatService;
import chatApp.services.chat.GroupChatServiceImpl;
import chatApp.services.chat.PrivateChatServiceImpl;
import chatApp.services.chat.RoomCharServiceImpl;

public class ChatServiceFactory {
    public static ChatService create(ChatType chatType) throws ClassNotFoundException{
        switch (chatType){
            case PRIVATE:
                return new PrivateChatServiceImpl();

        case ROOM:
                return new RoomCharServiceImpl();

            case GROUP :
                return new GroupChatServiceImpl();

            default : throw new ClassNotFoundException();
        }
    }
}
