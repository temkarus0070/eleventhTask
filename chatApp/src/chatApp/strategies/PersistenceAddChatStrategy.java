package chatApp.strategies;

import chatApp.domain.chat.Chat;
import chatApp.domain.chat.GroupChat;
import chatApp.domain.chat.PrivateChat;
import chatApp.domain.chat.RoomChat;
import chatApp.services.persistence.implementation.PersistenceChatServiceImpl;
import chatApp.services.persistence.implementation.PersistencePrivateChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceRoomChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class PersistenceAddChatStrategy {
    public Function<Map<String,String[]>, Chat> getAddMethod(Map<String,String[]> parameters){
        String chatType = parameters.get("chatType")[0];
        switch (chatType) {
            case "PrivateChat":
                return e-> {
                   Chat chat = new PrivateChat();
                    new PersistencePrivateChatServiceImpl().addChat(chat);
                    return chat;
                };
            case "RoomChat":
                return e-> {
                    String chatName=parameters.get("chatName")[0];
                    Chat chat = new RoomChat();
                    new PersistenceRoomChatServiceImpl().addChat(chat);
                    return chat;
                };

                chat = new RoomChat();
                ((RoomChat) chat).setName(chatName);
                persistenceChatService.addChat(chat);
                break;
            case "GroupChat":
                String chatName=parameters.get("chatName")[0];
                chat = new GroupChat();
                ((GroupChat) chat).setName(chatName);
                try {
                    ((GroupChat) chat).setUsersCount(Integer.parseInt(parameters.get("usersCount")[0]));
                } catch (Exception ex) {
                    resp.getOutputStream().print("users count is required");
                    return;
                }
                persistenceChatService.addChat(chat);
                break;
        }
    }
}
