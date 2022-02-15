package org.temkarus0070.application.factories;

import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.services.persistence.implementation.PersistenceChatServiceImpl;
import org.temkarus0070.application.services.persistence.implementation.PersistenceGroupChatServiceImpl;
import org.temkarus0070.application.services.persistence.implementation.PersistencePrivateChatServiceImpl;
import org.temkarus0070.application.services.persistence.implementation.PersistenceRoomChatServiceImpl;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceChatService;

public class PersistenceChatServiceFactory {
    public static PersistenceChatService create(ChatType chatType, ChatRepository chatRepository) throws ClassNotFoundException{
        PersistenceChatService persistenceChatService;
        switch ( chatType) {
            case PRIVATE:
                persistenceChatService = new PersistencePrivateChatServiceImpl(chatRepository);
                break;
            case ROOM:
                persistenceChatService = new PersistenceRoomChatServiceImpl(chatRepository);
                break;
            case GROUP:
                persistenceChatService = new PersistenceGroupChatServiceImpl(chatRepository);
                break;
            case ANY:
                persistenceChatService = new PersistenceChatServiceImpl(chatRepository);
                break;
            default:
                throw new ClassNotFoundException();
        }
        return persistenceChatService;
    }
}
