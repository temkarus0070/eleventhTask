package org.temkarus0070.application.services.persistence.statementExecutors;

import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.PrivateChat;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.services.persistence.ConnectionManager;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrivateChatStatementExecutor implements StatementExecutor<PrivateChat> {
    private Logger myLogger = Logger.getLogger(this.getClass().getName());
    @Override
    public void executeUpdate(PrivateChat chat) throws ChatAppDatabaseException {
        myLogger.log(Level.SEVERE, "PrivateChat can't be updated");
        throw new UnsupportedOperationException();
    }

    @Override
    public Chat executeAdd(PrivateChat chat) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Chats(chat_type,owner) VALUES ('PRIVATE',?)", Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setString(1, chat.getChatOwner().getUsername());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int id = resultSet.getInt(1);
            chat.setId(id);
            return chat;
        } catch (SQLException sqlException) {
            myLogger.log(Level.SEVERE, sqlException.getMessage());
            throw new ChatAppDatabaseException(sqlException);
        }
    }
}
