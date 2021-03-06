package chatApp.services.persistence.statementExecutors;

import chatApp.MyLogger;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.RoomChat;
import chatApp.domain.exceptions.ChatAppDatabaseException;
import chatApp.services.persistence.ConnectionManager;

import java.sql.*;
import java.util.logging.Level;

public class RoomChatStatementExecutor implements StatementExecutor<RoomChat> {
    @Override
    public void executeUpdate(RoomChat chat) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE chats SET name=? where id=?");) {
            RoomChat roomChat = chat;
            preparedStatement.setString(1, roomChat.getName());
            preparedStatement.setInt(2, roomChat.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            MyLogger.log(Level.SEVERE, sqlException.getMessage());
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
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }
}
