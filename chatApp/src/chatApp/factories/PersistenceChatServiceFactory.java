package chatApp.factories;

import chatApp.domain.chat.ChatType;
import chatApp.services.persistence.InMemoryUserStorage;
import chatApp.services.persistence.implementation.PersistenceChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceGroupChatServiceImpl;
import chatApp.services.persistence.implementation.PersistencePrivateChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceRoomChatServiceImpl;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.interfaces.PersistenceChatService;

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
