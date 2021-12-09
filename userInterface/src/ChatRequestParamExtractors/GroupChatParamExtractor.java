package ChatRequestParamExtractors;

import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.GroupChat;
import chatApp.services.persistence.ChatStorage;
import chatApp.services.persistence.implementation.PersistenceGroupChatServiceImpl;

import java.util.Map;
import java.util.Optional;

public class GroupChatParamExtractor implements ChatParamExtractor<GroupChat> {
    private PersistenceGroupChatServiceImpl persistenceGroupChatService = new PersistenceGroupChatServiceImpl(new ChatStorage(ChatType.GROUP));

    @Override
    public Optional<GroupChat> extractChat(Map<String, String[]> params) {
        int id = Integer.parseInt(params.get("chatId")[0]);
        Optional<GroupChat> groupChat = Optional.empty();
        if (params.get("chatName") != null) {
            String chatName = params.get("chatName")[0];
            if (chatName != null)
                groupChat = persistenceGroupChatService.getChatByName(chatName);
        } else
            groupChat = persistenceGroupChatService.getChat(id);
        return groupChat;
    }
}
