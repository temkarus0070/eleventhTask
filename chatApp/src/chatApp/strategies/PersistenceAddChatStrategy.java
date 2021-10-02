package chatApp.strategies;

import chatApp.domain.chat.*;
import chatApp.services.persistence.implementation.*;
import java.util.Map;
import java.util.function.Function;

public class PersistenceAddChatStrategy {
    public Function<Map<String,String[]>, Chat> getAddMethod(Map<String,String[]> parameters) throws ClassNotFoundException {
        String chatType = parameters.get("chatType")[0];
        switch (chatType) {
            case "PrivateChat":
                return e-> {
                   PrivateChat chat = new PrivateChat();
                    new PersistencePrivateChatServiceImpl().addChat(chat);
                    return chat;
                };
            case "RoomChat":
                return e-> {
                    String chatName=parameters.get("chatName")[0];
                    RoomChat chat = new RoomChat();
                    chat.setName(chatName);
                    new PersistenceRoomChatServiceImpl().addChat(chat);
                    return chat;
                };
            case "GroupChat":
                return e->{
                    String chatName=parameters.get("chatName")[0];
                    GroupChat chat = new GroupChat();
                    chat.setName(chatName);
                    new PersistenceGroupChatServiceImpl().addChat(chat);
                    return chat;
                };
        }
        throw new ClassNotFoundException();
    }
}
