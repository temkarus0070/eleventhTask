package chatApp.services.persistence;

import chatApp.MyLogger;
import chatApp.domain.chat.*;
import chatApp.domain.exceptions.ChatAppDatabaseException;
import chatApp.factories.StatementExecutorFactory;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.mappers.ChatExtractor;
import chatApp.services.persistence.mappers.ChatsExtractor;
import chatApp.services.persistence.mappers.Extractor;
import chatApp.services.persistence.statementExecutors.StatementExecutor;

import java.sql.*;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ChatStorage implements ChatRepository {
    private final ChatType chatType;
    private final Array array;

    private final Extractor<Chat> chatExtractor;

    private final ChatsExtractor chatsExtractor;

    public ChatStorage(ChatType chatType) throws ChatAppDatabaseException {
        chatExtractor = ChatExtractor.getInstance();
        chatsExtractor = ChatsExtractor.getInstance();
        this.chatType = chatType;
        try (Connection connection = ConnectionManager.getConnection()) {
            String[] array = chatType.getValues();
            this.array = connection.createArrayOf("text", array);
        } catch (SQLException sqlException) {
            MyLogger.log(Level.SEVERE, sqlException.getMessage());
            throw new ChatAppDatabaseException(sqlException);
        }


    }

    @Override
    public Collection<Chat> get() throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement statement = null;
            statement = connection.prepareStatement("SELECT * FROM CHATS where chat_type::text in ? ORDER BY id");
            statement.setArray(1, array);
            ResultSet resultSet = statement.executeQuery();
            statement.close();
            return chatsExtractor.extract(resultSet);
        } catch (SQLException sqlException) {
            MyLogger.log(Level.SEVERE, sqlException.getMessage());
            throw new ChatAppDatabaseException(sqlException);
        }
    }

    public Collection<Chat> getChatsByUser(String username) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement("SELECT * FROM users u " +
                    "INNER JOIN  users_chats uc ON u.username=uc.username " +
                    "INNER JOIN  Chats c ON c.id=uc.chat_id " +
                    "WHERE u.username=? and c.chat_type::text  = any(?::text[]) " +
                    "ORDER BY c.id");

            preparedStatement.setArray(2, array);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            preparedStatement.close();
            return chatsExtractor.extract(resultSet);
        } catch (SQLException throwables) {
            MyLogger.log(Level.SEVERE, throwables.getMessage());
            throw new ChatAppDatabaseException(throwables);
        }

    }

    @Override
    public Optional<Chat> getChatByName(String name) throws ChatAppDatabaseException {
        if (chatType == ChatType.PRIVATE)
            throw new UnsupportedOperationException();
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement("SELECT * FROM chats as c INNER JOIN users_chats as usChats" +
                    " on c.id=usChats.chat_id " +
                    "LEFT JOIN  messages as m on (m.chat_id=c.id and m.sender_name=usChats.username) " +
                    " WHERE c.name=? and c.chat_type::text = any(?::text[])");
            preparedStatement.setArray(2, array);
            preparedStatement.setString(1, name);

            Chat chat = chatExtractor.extract(preparedStatement.executeQuery());
            preparedStatement.close();
            return Optional.ofNullable(chat);
        } catch (SQLException exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void removeUserFromChat(String user, Integer chatId) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users_chats WHERE (username=? AND chat_id=?)");
            preparedStatement.setInt(2, chatId);
            preparedStatement.setString(1, user);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException chatAppDatabaseException) {
            MyLogger.log(Level.SEVERE, chatAppDatabaseException.getMessage());
            throw new ChatAppDatabaseException(chatAppDatabaseException);
        }
    }

    @Override
    public void addUserToChat(String user, Integer chatId) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users_chats(username,chat_id,has_ban) values(?,?,false)");
            preparedStatement.setString(1, user);
            preparedStatement.setInt(2, chatId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void banUserInChat(String user, Integer chatId) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users_chats SET has_ban=true " +
                    "where (username=? AND chat_id=?)");
            preparedStatement.setString(1, user);
            preparedStatement.setInt(2, chatId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void addMessage(Message message, Integer chatId) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages(text,sender_name,chat_id) values (?,?,?)");
            preparedStatement.setString(1, message.getContent());
            preparedStatement.setString(2, message.getSender().getName());
            preparedStatement.setInt(3, chatId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public Chat get(Integer integer) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chats as c INNER JOIN users_chats as usChats" +
                    " on c.id=usChats.chat_id " +
                    "LEFT JOIN  messages as m on (m.chat_id=c.id and m.sender_name=usChats.username) " +
                    " WHERE c.id=? AND c.chat_type::text = any(?::text[])");
            preparedStatement.setArray(2, array);
            preparedStatement.setInt(1, integer);
            preparedStatement.close();
            return chatExtractor.extract(preparedStatement.executeQuery());
        } catch (SQLException exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void add(Chat entity) throws ChatAppDatabaseException {
        StatementExecutor statementExecutor = StatementExecutorFactory.getStatementPreparator(entity.getType());
        entity = statementExecutor.executeAdd(entity);
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO users_chats VALUES (?,?,false)");
            preparedStatement1.setString(1, entity.getChatOwner().getName());
            preparedStatement1.setInt(2, entity.getId());
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
        } catch (SQLException exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }


    @Override
    public void delete(Integer integer) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM chats where id=?");
            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void update(Chat entity) throws ChatAppDatabaseException {
        StatementExecutor statementExecutor = StatementExecutorFactory.getStatementPreparator(entity.getType());
        statementExecutor.executeUpdate(entity);
    }
}
