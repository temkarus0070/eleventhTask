package chatApp.strategies;

import chatApp.domain.chat.Chat;
import chatApp.services.chat.PrivateChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceGroupChatServiceImpl;
import chatApp.services.persistence.implementation.PersistencePrivateChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceRoomChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class PersistenceGetChatStrategy {
    public Function<Map<String, String[]>,Optional<Chat>> takeGetMethod(Map<String, String[]> parameterMap) throws ClassNotFoundException {
            String type = parameterMap.get("chatType")[0];
            Optional<Chat> chat;
            switch (type) {
                case "PrivateChat":
                    return e->{
                       PersistencePrivateChatServiceImpl privateChatService=new PersistencePrivateChatServiceImpl();
                        int id = Integer.parseInt(e.get("chatId")[0]);
                        return privateChatService.getChat(id);
                    };
                case "GroupChat":
                    return e->{
                       PersistenceGroupChatServiceImpl persistenceGroupChatService= new PersistenceGroupChatServiceImpl();
                        return persistenceGroupChatService.getChatByName(e.get("chatName")[0]);
                    };
                case "RoomChat":
                    return e->{
                        PersistenceRoomChatServiceImpl persistenceRoomChatService=new PersistenceRoomChatServiceImpl();
                        return persistenceRoomChatService.getChatByName(e.get("chatName")[0]);
                    };
            }
            throw new ClassNotFoundException();
    }
}
