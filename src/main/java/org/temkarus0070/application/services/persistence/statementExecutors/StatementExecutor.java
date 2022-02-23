package org.temkarus0070.application.services.persistence.statementExecutors;

import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;

public interface StatementExecutor<T extends Chat> {
    public void executeUpdate(T chat) throws ChatAppDatabaseException;

    public Chat executeAdd(Chat chat) throws ChatAppDatabaseException;
}
