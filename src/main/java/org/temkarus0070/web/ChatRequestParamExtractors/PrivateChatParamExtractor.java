package org.temkarus0070.web.ChatRequestParamExtractors;

import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.chat.PrivateChat;
import org.temkarus0070.application.services.persistence.ChatStorage;
import org.temkarus0070.application.services.persistence.implementation.PersistencePrivateChatServiceImpl;

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

