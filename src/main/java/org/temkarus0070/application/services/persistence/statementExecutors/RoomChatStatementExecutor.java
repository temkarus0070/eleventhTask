package org.temkarus0070.application.services.persistence.statementExecutors;

import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.RoomChat;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.services.persistence.ConnectionManager;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomChatStatementExecutor implements StatementExecutor<RoomChat> {
    private Logger myLogger = Logger.getLogger(this.getClass().getName());
    @Override
    public void executeUpdate(RoomChat chat) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE chats SET name=? where id=?");) {
            RoomChat roomChat = chat;
            preparedStatement.setString(1, roomChat.getName());
            preparedStatement.setInt(2, roomChat.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            myLogger.log(Level.SEVERE, sqlException.getMessage());
            throw new ChatAppDatabaseException(sqlException);
        }

    }

    @Override
    public Chat executeAdd(RoomChat chat) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Chats(chat_type,name,owner) VALUES ('ROOM',?,?)", Statement.RETURN_GENERATED_KEYS);) {
            RoomChat roomChat = chat;

            preparedStatement.setString(1, roomChat.getName());
            preparedStatement.setString(2, roomChat.getChatOwner().getName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int id = resultSet.getInt(1);
            chat.setId(id);
            return chat;
        } catch (SQLException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }
}
