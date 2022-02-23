package org.temkarus0070.web.ChatRequestParamExtractors;

import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.exceptions.ClassOfChatAppNotFoundException;
import org.temkarus0070.application.services.persistence.ChatStorage;

public class ChatRequestParamExtractorBuilder {
    public static ChatParamExtractor build(ChatType chatType, ChatStorage chatStorage) {
        switch (chatType) {
            case PRIVATE:
                return new PrivateChatParamExtractor(chatStorage);
            case ROOM:
                return new RoomChatParamExtractor(chatStorage);
            case GROUP:
                return new GroupChatParamExtractor(chatStorage);
        }
        throw new ClassOfChatAppNotFoundException();
    }
}
