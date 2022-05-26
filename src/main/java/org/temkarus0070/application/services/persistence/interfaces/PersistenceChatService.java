package org.temkarus0070.application.services.persistence.interfaces;

import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.Message;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;

import java.util.Collection;
import java.util.Optional;

public interface PersistenceChatService<T extends Chat> {


    public Optional<T> getChatByName(String name) throws ChatAppDatabaseException;

    public Optional<T> getChat(int id) throws ChatAppDatabaseException;

    public void removeChat(int id) throws ChatAppDatabaseException;

    public Collection<T> get() throws ChatAppDatabaseException;

    public void updateChat(T chat) throws ChatAppDatabaseException;

    public Collection<T> getChatsByUserName(String username) throws ChatAppDatabaseException;

    public void addChat(T chat) throws ChatAppDatabaseException;

    public void addUser(String username, int chatId) throws ChatAppDatabaseException;

    public void banUserInChat(String username, int chatId) throws ChatAppDatabaseException;

    public void removeUserFromChat(String username, int chatId) throws ChatAppDatabaseException;

    public void addMessage(Message message, int chatId) throws ChatAppDatabaseException;
}
