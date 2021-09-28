package chatApp.services;

import chatApp.domain.chat.GroupChat;
import chatApp.domain.chat.PrivateChat;
import chatApp.domain.chat.RoomChat;
import chatApp.services.chat.ChatService;
import chatApp.services.chat.GroupChatServiceImpl;
import chatApp.services.chat.PrivateChatServiceImpl;
import chatApp.services.chat.RoomCharServiceImpl;

import java.util.Map;

public class ChatServiceSelector {
    public ChatService getChatService(  Map<String, String[]> parameters){
        String chatType=parameters.get("chatType")[0];
        switch (chatType){
            case "RoomChat":
                return new RoomCharServiceImpl();
                break;
            case "GroupChat":
                return new GroupChatServiceImpl();
              break;
            default:
                return new PrivateChatServiceImpl();

        }
    }
}
