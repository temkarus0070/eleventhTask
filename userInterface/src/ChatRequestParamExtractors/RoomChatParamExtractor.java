package ChatRequestParamExtractors;

import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.RoomChat;
import chatApp.services.persistence.ChatStorage;
import chatApp.services.persistence.implementation.PersistenceRoomChatServiceImpl;

import java.util.Map;
import java.util.Optional;

public class RoomChatParamExtractor implements ChatParamExtractor<RoomChat> {
    private PersistenceRoomChatServiceImpl persistenceRoomChatService = new PersistenceRoomChatServiceImpl(new ChatStorage(ChatType.ROOM));

    @Override
    public Optional<RoomChat> extractChat(Map<String, String[]> params) {
        Optional<RoomChat> roomChat = Optional.empty();
        int id = Integer.parseInt(params.get("chatId")[0]);
        if (params.get("chatName") != null) {
            String chatName = params.get("chatName")[0];
            if (chatName != null)
                roomChat = persistenceRoomChatService.getChatByName(chatName);
        } else
            roomChat = persistenceRoomChatService.getChat(id);
        return roomChat;
    }
}
