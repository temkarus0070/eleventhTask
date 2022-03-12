package org.temkarus0070.application.services.persistence.statementExecutors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.RoomChat;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RoomChatStatementExecutor implements StatementExecutor<RoomChat> {
    private Logger myLogger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void executeUpdate(RoomChat chat) throws ChatAppDatabaseException {
        try {
            jdbcTemplate.update((connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE chats SET name=? where id=?");
                preparedStatement.setString(1, chat.getName());
                preparedStatement.setInt(2, chat.getId());
                return preparedStatement;
            });
        } catch (DataAccessException sqlException) {
            myLogger.log(Level.SEVERE, sqlException.getMessage());
            throw new ChatAppDatabaseException(sqlException);
        }

    }

    @Override
    public Chat executeAdd(Chat chat1) throws ChatAppDatabaseException {
        RoomChat chat = (RoomChat) chat1;
        try {
            RoomChat finalChat = chat;
            return jdbcTemplate.execute((PreparedStatementCreator) (connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Chats(chat_type,name,owner) VALUES ('ROOM',?,?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, finalChat.getName());
                preparedStatement.setString(2, finalChat.getChatOwner().getUsername());
                return preparedStatement;
            }, (preparedStatement) -> {

                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                int id = resultSet.getInt(1);
                finalChat.setId(id);
                return finalChat;
            });
        } catch (DataAccessException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }
}
