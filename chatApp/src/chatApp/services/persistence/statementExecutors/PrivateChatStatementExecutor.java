package chatApp.services.persistence.statementExecutors;

import chatApp.MyLogger;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.PrivateChat;
import chatApp.domain.exceptions.ChatAppDatabaseException;
import chatApp.services.persistence.ConnectionManager;

import java.sql.*;
import java.util.logging.Level;

public class PrivateChatStatementExecutor implements StatementExecutor<PrivateChat> {
    @Override
    public void executeUpdate(PrivateChat chat) throws ChatAppDatabaseException {
        MyLogger.log(Level.SEVERE, "PrivateChat can't be updated");
        throw new UnsupportedOperationException();
    }

    @Override
    public Chat executeAdd(PrivateChat chat) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Chats(chat_type,owner) VALUES ('PRIVATE',?)", Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setString(1, chat.getChatOwner().getName());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int id = resultSet.getInt(1);
            chat.setId(id);
            return chat;
        } catch (SQLException sqlException) {
            MyLogger.log(Level.SEVERE, sqlException.getMessage());
            throw new ChatAppDatabaseException(sqlException);
        }
    }
}
