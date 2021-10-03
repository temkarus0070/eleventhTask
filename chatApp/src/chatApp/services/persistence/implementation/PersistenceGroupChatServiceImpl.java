package chatApp.services.persistence.implementation;

import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.GroupChat;

import java.util.Optional;

public class PersistenceGroupChatServiceImpl extends PersistenceChatServiceImpl<GroupChat> {
    public Optional<GroupChat> getChatByName(String name) {
        return mockGetChatByName(name);
    }


    private Optional<GroupChat> mockGetChatByName(String name){
        return get().stream().filter(e->{
            if(e.getType()== ChatType.GROUP ){
                GroupChat roomChat=(GroupChat) e;
                return roomChat.getName().equals(name);
            }
            else
                return false;
        }).findFirst();
    }
}
