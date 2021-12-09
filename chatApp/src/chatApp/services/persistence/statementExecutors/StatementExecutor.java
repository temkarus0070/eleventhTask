package chatApp.services.persistence.statementExecutors;

import chatApp.domain.chat.Chat;
import chatApp.domain.exceptions.ChatAppDatabaseException;

public interface StatementExecutor<T extends Chat> {
    public void executeUpdate(T chat) throws ChatAppDatabaseException;

    public Chat executeAdd(T chat) throws ChatAppDatabaseException;
}
