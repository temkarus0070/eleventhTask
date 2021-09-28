package chatApp.services.persistence.implementation;

import chatApp.domain.chat.Chat;
import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.GroupChat;
import chatApp.domain.chat.RoomChat;

import java.util.Optional;

public class PersistenceRoomChatServiceImpl extends PersistenceChatServiceImpl{
    public Optional<Chat> getChatByName(String name) {
        return mockGetChatByName(name);
    }


    private Optional<Chat> mockGetChatByName(String name){
        return get().stream().filter(e->{
            if(e.getType()== ChatType.ROOM){
                RoomChat roomChat=(RoomChat) e;
                return roomChat.getName().equals(name);
            }
            else
                return false;
        }).findFirst();
    }
}
