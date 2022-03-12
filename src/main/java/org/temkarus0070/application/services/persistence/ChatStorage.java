package org.temkarus0070.application.services.persistence;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.chat.Message;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.factories.StatementExecutorFactory;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.mappers.ChatsExtractor;
import org.temkarus0070.application.services.persistence.mappers.Extractor;
import org.temkarus0070.application.services.persistence.statementExecutors.StatementExecutor;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ChatStorage implements ChatRepository {
    private JdbcTemplate jdbcTemplate;
    private Logger myLogger = Logger.getLogger(ChatStorage.class.getName());

    private Array array;

    private final Extractor<Chat> chatExtractor;

    private final ChatsExtractor chatsExtractor;

    private final StatementExecutorFactory statementExecutorFactory;

    public ChatStorage(JdbcTemplate jdbcTemplate, Extractor<Chat> chatExtractor, ChatsExtractor chatsExtractor, StatementExecutorFactory statementExecutorFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.chatExtractor = chatExtractor;
        this.chatsExtractor = chatsExtractor;
        this.statementExecutorFactory = statementExecutorFactory;
    }

    private void setChatType(ChatType chatType) {
        jdbcTemplate.execute((ConnectionCallback<Object>) con -> {
            String[] array = chatType.getValues();
            this.array = con.createArrayOf("text", array);
            return null;
        });
    }


    @Override
    public Collection<Chat> get(ChatType chatType) throws ChatAppDatabaseException {
        setChatType(chatType);
        try {
            return jdbcTemplate.execute((connection) -> {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM CHATS where chat_type::text in ? ORDER BY id");
                statement.setArray(1, array);
                return statement;
            }, (PreparedStatementCallback<Collection<Chat>>) ps -> chatsExtractor.extract(ps.executeQuery()));

        } catch (org.springframework.dao.DataAccessException DataAccessException) {
            myLogger.log(Level.SEVERE, DataAccessException.getMessage());
            throw new ChatAppDatabaseException(DataAccessException);
        }
    }

    @Override
    public Collection<Chat> getChatsByUser(String username, ChatType chatType) throws ChatAppDatabaseException {
        setChatType(chatType);
        try {
            return jdbcTemplate.execute((connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users u " +
                        "INNER JOIN  users_chats uc ON u.username=uc.username " +
                        "INNER JOIN  Chats c ON c.id=uc.chat_id " +
                        "WHERE u.username=? and c.chat_type::text  = any(?::text[]) " +
                        "ORDER BY c.id");
                preparedStatement.setArray(2, array);
                preparedStatement.setString(1, username);
                return preparedStatement;
            }, (PreparedStatementCallback<Collection<Chat>>) (preparedStatement) -> chatsExtractor.extract(preparedStatement.executeQuery()));
        } catch (org.springframework.dao.DataAccessException throwables) {
            myLogger.log(Level.SEVERE, throwables.getMessage());
            throw new ChatAppDatabaseException(throwables);
        }

    }

    @Override
    public Optional<Chat> getChatByName(String name, ChatType chatType) throws ChatAppDatabaseException {
        setChatType(chatType);
        if (chatType == ChatType.PRIVATE)
            throw new UnsupportedOperationException();
        try {
            return Optional.ofNullable(jdbcTemplate.execute((connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chats as c INNER JOIN users_chats as usChats" +
                        " on c.id=usChats.chat_id " +
                        "LEFT JOIN  messages as m on (m.chat_id=c.id and m.sender_name=usChats.username) " +
                        " WHERE c.name=? and c.chat_type::text = any(?::text[])");
                preparedStatement.setArray(2, array);
                preparedStatement.setString(1, name);
                return preparedStatement;
            }, (PreparedStatementCallback<Chat>) (preparedStatement) -> chatExtractor.extract(preparedStatement.executeQuery())));
        } catch (DataAccessException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void removeUserFromChat(String user, Integer chatId) throws ChatAppDatabaseException {
        try {
            jdbcTemplate.update((connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users_chats WHERE (username=? AND chat_id=?)");
                preparedStatement.setInt(2, chatId);
                preparedStatement.setString(1, user);
                return preparedStatement;
            });
        } catch (DataAccessException chatAppDatabaseException) {
            myLogger.log(Level.SEVERE, chatAppDatabaseException.getMessage());
            throw new ChatAppDatabaseException(chatAppDatabaseException);
        }
    }

    @Override
    public void addUserToChat(String user, Integer chatId) throws ChatAppDatabaseException {
        try {
            jdbcTemplate.update((connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users_chats(username,chat_id,has_ban) values(?,?,false)");
                preparedStatement.setString(1, user);
                preparedStatement.setInt(2, chatId);
                return preparedStatement;
            });
        } catch (DataAccessException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void banUserInChat(String user, Integer chatId) throws ChatAppDatabaseException {
        try {
            jdbcTemplate.update((connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users_chats SET has_ban=true " +
                        "where (username=? AND chat_id=?)");
                preparedStatement.setString(1, user);
                preparedStatement.setInt(2, chatId);
                return preparedStatement;
            });
        } catch (DataAccessException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void addMessage(Message message, Integer chatId) throws ChatAppDatabaseException {
        try {
            jdbcTemplate.update((connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages(text,sender_name,chat_id) values (?,?,?)");
                preparedStatement.setString(1, message.getContent());
                preparedStatement.setString(2, message.getSender().getUsername());
                preparedStatement.setInt(3, chatId);
                return preparedStatement;
            });
        } catch (DataAccessException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public Chat get(Integer id, ChatType chatType) throws ChatAppDatabaseException {
        setChatType(chatType);
        try {
            return jdbcTemplate.execute((connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chats as c INNER JOIN users_chats as usChats" +
                        " on c.id=usChats.chat_id " +
                        "LEFT JOIN  messages as m on (m.chat_id=c.id and m.sender_name=usChats.username) " +
                        " WHERE c.id=? AND c.chat_type::text = any(?::text[])");
                preparedStatement.setArray(2, array);
                preparedStatement.setInt(1, id);
                return preparedStatement;
            }, (PreparedStatementCallback<Chat>) (preparedStatement) -> chatExtractor.extract(preparedStatement.executeQuery()));


        } catch (DataAccessException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void add(Chat entity) throws ChatAppDatabaseException {
        StatementExecutor statementExecutor = statementExecutorFactory.getStatementPreparator(entity.getType());
        entity = statementExecutor.executeAdd(entity);
        try {
            Chat finalEntity = entity;
            jdbcTemplate.update((connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users_chats VALUES (?,?,false)");
                preparedStatement.setString(1, finalEntity.getChatOwner().getUsername());
                preparedStatement.setInt(2, finalEntity.getId());
                return preparedStatement;
            });
        } catch (DataAccessException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }


    @Override
    public void delete(Integer id) throws ChatAppDatabaseException {
        try {
            jdbcTemplate.update((PreparedStatementCreator) (connection) -> {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM chats where id=?");
                preparedStatement.setInt(1, id);
                return preparedStatement;
            });
        } catch (DataAccessException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void update(Chat entity) throws ChatAppDatabaseException {
        StatementExecutor statementExecutor = statementExecutorFactory.getStatementPreparator(entity.getType());
        statementExecutor.executeUpdate(entity);
    }
}
