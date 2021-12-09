package ChatRequestParamExtractors;

import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.PrivateChat;
import chatApp.services.persistence.ChatStorage;
import chatApp.services.persistence.implementation.PersistencePrivateChatServiceImpl;

import java.util.Map;
import java.util.Optional;

public class PrivateChatParamExtractor implements ChatParamExtractor<PrivateChat> {
    private final PersistencePrivateChatServiceImpl persistencePrivateChatService = new PersistencePrivateChatServiceImpl(new ChatStorage(ChatType.PRIVATE));

    @Override
    public Optional<PrivateChat> extractChat(Map<String, String[]> params) {
        Optional<PrivateChat> chat = Optional.empty();

        if (params.get("chatId") != null) {
            int id = Integer.parseInt(params.get("chatId")[0]);

            chat = persistencePrivateChatService.getChat(id);
        }
        return chat;
    }

    @Override
    public Optional<PrivateChat> putChat(Map<String, String[]> params) {
        Optional<PrivateChat> optionalPrivateChat = Optional.of(new PrivateChat());
        return optionalPrivateChat;
    }


}

