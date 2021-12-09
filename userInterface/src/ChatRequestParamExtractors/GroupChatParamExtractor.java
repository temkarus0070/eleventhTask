package ChatRequestParamExtractors;

import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.GroupChat;
import chatApp.services.persistence.ChatStorage;
import chatApp.services.persistence.implementation.PersistenceGroupChatServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class GroupChatParamExtractor implements ChatParamExtractor<GroupChat> {
    private PersistenceGroupChatServiceImpl persistenceGroupChatService = new PersistenceGroupChatServiceImpl(new ChatStorage(ChatType.GROUP));

    @Override
    public Optional<GroupChat> extractChat(Map<String, String[]> params) {
        Optional<GroupChat> optionalGroupChat = Optional.empty();
        if (params.get("chatId") != null) {
            int id = Integer.parseInt(params.get("chatId")[0]);
            optionalGroupChat = persistenceGroupChatService.getChat(id);
        } else if (params.get("chatName") != null) {
            optionalGroupChat = persistenceGroupChatService.getChatByName(params.get("chatName")[0]);
        }
        return optionalGroupChat;
    }

    @Override
    public Optional<GroupChat> putChat(Map<String, String[]> params) {
        Optional<GroupChat> optionalGroupChat = Optional.empty();
        String chatName = params.get("chatName")[0];
        GroupChat groupChat = new GroupChat();
        groupChat.setName(chatName);
        groupChat.setUsersCount(Integer.parseInt(params.get("usersCount")[0]));
        optionalGroupChat = Optional.of(groupChat);
        return optionalGroupChat;
    }
}
