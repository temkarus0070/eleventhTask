package org.temkarus0070.web.ChatRequestParamExtractors;

import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.exceptions.ClassOfChatAppNotFoundException;

import java.util.Map;

public class ChatRequestParamExtractorBuilder {
    public static ChatParamExtractor build(Map<String, String[]> params) {
        ChatType chatType = ChatType.valueOf(params.get("chatType")[0]);
        switch (chatType) {
            case PRIVATE:
                return new PrivateChatParamExtractor();
            case ROOM:
                return new RoomChatParamExtractor();
            case GROUP:
                return new GroupChatParamExtractor();
        }
        throw new ClassOfChatAppNotFoundException();
    }
}
