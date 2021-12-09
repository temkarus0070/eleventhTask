package chatApp.services.persistence.statementExecutors;

import chatApp.domain.chat.Chat;
import chatApp.domain.chat.GroupChat;
import chatApp.domain.exceptions.ChatAppDatabaseException;
import chatApp.services.persistence.ConnectionManager;

import java.sql.*;

public class GroupChatStatementExecutor implements StatementExecutor<GroupChat> {
    @Override
    public void executeUpdate(GroupChat chat) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            GroupChat groupChat = chat;
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE chats SET name=? and users_count=? where id=?");
            preparedStatement.setString(1, groupChat.getName());
            preparedStatement.setInt(2, groupChat.getUsersCount());
            preparedStatement.setInt(3, groupChat.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new ChatAppDatabaseException(sqlException);
        }
    }

    @Override
    public Chat executeAdd(GroupChat chat) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            GroupChat groupChat = chat;
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chats(chat_type, name, users_count,owner) VALUES (" +
                    "'GROUP',?,?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, groupChat.getName());
            preparedStatement.setInt(2, groupChat.getUsersCount());
            preparedStatement.setString(3, groupChat.getChatOwner().getName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int id = resultSet.getInt(1);
            chat.setId(id);
            return chat;
        } catch (SQLException e) {
            throw new ChatAppDatabaseException(e);
        }
    }
}
