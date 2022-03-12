package org.temkarus0070.application.services.persistence.statementExecutors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.PrivateChat;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PrivateChatStatementExecutor implements StatementExecutor<PrivateChat> {
    private Logger myLogger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void executeUpdate(PrivateChat chat) throws ChatAppDatabaseException {
        myLogger.log(Level.SEVERE, "PrivateChat can't be updated");
        throw new UnsupportedOperationException();
    }

    @Override
    public Chat executeAdd(Chat chat1) throws ChatAppDatabaseException {
        PrivateChat chat = (PrivateChat) chat1;
        try {
            final PrivateChat finalChat = chat;
            chat = jdbcTemplate.execute((PreparedStatementCreator) (connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Chats(chat_type,owner) VALUES ('PRIVATE',?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, finalChat.getChatOwner().getUsername());
                preparedStatement.executeUpdate();
                return preparedStatement;
            }, (preparedStatement) -> {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                int id = resultSet.getInt(1);
                finalChat.setId(id);
                ;
                return finalChat;
            });
            return chat;
        } catch (DataAccessException sqlException) {
            myLogger.log(Level.SEVERE, sqlException.getMessage());
            throw new ChatAppDatabaseException(sqlException);
        }
    }
}
