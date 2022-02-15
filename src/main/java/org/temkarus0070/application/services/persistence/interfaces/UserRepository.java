package org.temkarus0070.application.services.persistence.interfaces;

import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;

import java.util.Collection;

public interface UserRepository extends Repository<User,String>{
    public Collection<User> getUsersNotAtThatChat(Integer chatId) throws ChatAppDatabaseException;
}
