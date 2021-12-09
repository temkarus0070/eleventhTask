package ChatRequestParamExtractors;

import chatApp.domain.chat.ChatType;

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
    }
}
