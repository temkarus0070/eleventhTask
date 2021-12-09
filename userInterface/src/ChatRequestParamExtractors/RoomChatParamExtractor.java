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
        if (params.get("chatId") != null) {
            int id = Integer.parseInt(params.get("chatId")[0]);
            roomChat = persistenceRoomChatService.getChat(id);
        } else if (params.get("chatName") != null) {
            String chatName = params.get("chatName")[0];
            if (chatName != null)
                roomChat = persistenceRoomChatService.getChatByName(chatName);

        }
        return roomChat;
    }

    @Override
    public Optional<RoomChat> putChat(Map<String, String[]> params) {
        Optional<RoomChat> roomChatOptional = Optional.of(new RoomChat());
        String chatName = params.get("chatName")[0];
        roomChatOptional.get().setName(chatName);
        return roomChatOptional;
    }
}
