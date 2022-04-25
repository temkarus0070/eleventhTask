package org.temkarus0070.application.services.persistence.interfaces;

import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.chat.Message;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;

import java.util.Collection;
import java.util.Optional;

public interface ChatRepository {
    public Collection<Chat> getChatsByUser(String username, ChatType chatType) throws ChatAppDatabaseException;

    public void add(Chat entity) throws ChatAppDatabaseException;

    Collection<Chat> get(ChatType chatType) throws ChatAppDatabaseException;


    public Optional<Chat> getChatByName(String name, ChatType chatType) throws ChatAppDatabaseException;

    public void removeUserFromChat(String user, Integer chatId) throws ChatAppDatabaseException;

    public void addUserToChat(String user, Integer chatId) throws ChatAppDatabaseException;

    public void banUserInChat(String user, Integer chatId) throws ChatAppDatabaseException;

    public void addMessage(Message message, Integer chatId) throws ChatAppDatabaseException;

    public void delete(Integer id) throws ChatAppDatabaseException;

    public void update(Chat entity) throws ChatAppDatabaseException;

    Chat get(Integer id, ChatType chatType) throws ChatAppDatabaseException;
}
