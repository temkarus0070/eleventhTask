package org.temkarus0070.application.services.persistence.interfaces;

import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;

import java.util.Collection;
import java.util.Optional;

public interface PersistenceUserService {
    public void addUser(User user) throws ChatAppDatabaseException;

    public Optional<User> getUser(String userName) throws ChatAppDatabaseException;


    public Collection<User> get() throws ChatAppDatabaseException;

    public void updateUser(User user) throws ChatAppDatabaseException;

    public void deleteUser(String username) throws ChatAppDatabaseException;

    public Collection<User> getUsersNotAtThatChat(Integer chatId) throws ChatAppDatabaseException;

}
