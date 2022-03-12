package org.temkarus0070.application.services.persistence.statementExecutors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.GroupChat;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class GroupChatStatementExecutor implements StatementExecutor<GroupChat> {
    private Logger myLogger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void executeUpdate(GroupChat chat) throws ChatAppDatabaseException {
        try {
            jdbcTemplate.update((connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE chats SET name=? and users_count=? where id=?");
                preparedStatement.setString(1, chat.getName());
                preparedStatement.setInt(2, chat.getUsersCount());
                preparedStatement.setInt(3, chat.getId());
                return preparedStatement;
            });

        } catch (DataAccessException sqlException) {
            myLogger.log(Level.SEVERE, sqlException.getMessage());
            throw new ChatAppDatabaseException(sqlException);
        }
    }

    @Override
    public Chat executeAdd(Chat chat1) throws ChatAppDatabaseException {
        GroupChat chat = (GroupChat) chat1;
        final GroupChat finalChat = chat;

        try {
            GroupChat finalChat1 = chat;
            chat = jdbcTemplate.execute((PreparedStatementCreator) (connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chats(chat_type, name, users_count,owner) VALUES (" +
                        "'GROUP',?,?,?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, finalChat.getName());
                preparedStatement.setInt(2, finalChat.getUsersCount());
                preparedStatement.setString(3, finalChat.getChatOwner().getUsername());
                return preparedStatement;
            }, (preparedStatement) -> {
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                int id = resultSet.getInt(1);
                finalChat.setId(id);
                return finalChat;
            });
            return chat;
        } catch (DataAccessException e) {
            myLogger.log(Level.SEVERE, e.getMessage());
            throw new ChatAppDatabaseException(e);
        }
    }
}
