package org.temkarus0070.application.services.persistence.statementExecutors;

import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.GroupChat;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.services.persistence.ConnectionManager;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupChatStatementExecutor implements StatementExecutor<GroupChat> {
    private Logger myLogger = Logger.getLogger(this.getClass().getName());

    @Override
    public void executeUpdate(GroupChat chat) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE chats SET name=? and users_count=? where id=?");) {
            GroupChat groupChat = chat;

            preparedStatement.setString(1, groupChat.getName());
            preparedStatement.setInt(2, groupChat.getUsersCount());
            preparedStatement.setInt(3, groupChat.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            myLogger.log(Level.SEVERE, sqlException.getMessage());
            throw new ChatAppDatabaseException(sqlException);
        }
    }

    @Override
    public Chat executeAdd(Chat chat1) throws ChatAppDatabaseException {
        GroupChat chat = (GroupChat) chat1;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chats(chat_type, name, users_count,owner) VALUES (" +
                     "'GROUP',?,?,?)", Statement.RETURN_GENERATED_KEYS);) {
            GroupChat groupChat = chat;

            preparedStatement.setString(1, groupChat.getName());
            preparedStatement.setInt(2, groupChat.getUsersCount());
            preparedStatement.setString(3, groupChat.getChatOwner().getUsername());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int id = resultSet.getInt(1);
            chat.setId(id);
            return chat;
        } catch (SQLException e) {
            myLogger.log(Level.SEVERE, e.getMessage());
            throw new ChatAppDatabaseException(e);
        }
    }
}
