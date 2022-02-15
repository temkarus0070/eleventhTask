package org.temkarus0070.application.services.persistence.interfaces;

import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.Message;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;

import java.util.Collection;
import java.util.Optional;

public interface ChatRepository extends Repository<Chat, Integer> {
    public Collection<Chat> getChatsByUser(String username) throws ChatAppDatabaseException;

    public Optional<Chat> getChatByName(String name) throws ChatAppDatabaseException;

    public void removeUserFromChat(String user, Integer chatId) throws ChatAppDatabaseException;

    public void addUserToChat(String user, Integer chatId) throws ChatAppDatabaseException;

    public void banUserInChat(String user, Integer chatId) throws ChatAppDatabaseException;

    public void addMessage(Message message, Integer chatId) throws ChatAppDatabaseException;
}
