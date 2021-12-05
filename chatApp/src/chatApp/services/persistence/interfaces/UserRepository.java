package chatApp.services.persistence.interfaces;

import chatApp.domain.User;
import chatApp.domain.exceptions.ChatAppDatabaseException;

import java.util.Collection;

public interface UserRepository extends Repository<User,String>{
    public Collection<User> getUsersNotAtThatChat(Integer chatId) throws ChatAppDatabaseException;
}
