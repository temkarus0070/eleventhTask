package chatApp.services.persistence.interfaces;

import chatApp.domain.chat.Chat;

import java.util.Optional;

public interface PersistenceNameableChatService extends PersistenceChatService {
    public Optional<Chat> getChatByName(String name);
}
