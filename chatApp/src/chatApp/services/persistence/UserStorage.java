package chatApp.services.persistence;

import chatApp.MyLogger;
import chatApp.domain.User;
import chatApp.domain.exceptions.ChatAppDatabaseException;
import chatApp.services.persistence.interfaces.UserRepository;
import chatApp.services.persistence.mappers.UserExtractor;
import chatApp.services.persistence.mappers.UsersExtractor;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

public class UserStorage implements UserRepository {
    private UsersExtractor usersExtractor=UsersExtractor.getInstance();
    private UserExtractor userExtractor=UserExtractor.getInstance();


    public UserStorage() {
    }

    @Override
    public Collection<User> get() throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            return usersExtractor.extract(resultSet);
        } catch (SQLException | ChatAppDatabaseException exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public User get(String s) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users where username=?");
            preparedStatement.setString(1, s);
            return userExtractor.extract(preparedStatement.executeQuery());
        } catch (SQLException | ChatAppDatabaseException exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void add(User entity) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(username,password) VALUES (?,?)");
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException | ChatAppDatabaseException exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }


    @Override
    public void delete(String s) throws ChatAppDatabaseException {

    }

    @Override
    public void update(User entity) throws ChatAppDatabaseException {

    }

    @Override
    public Collection<User> getUsersNotAtThatChat(Integer chatId) throws ChatAppDatabaseException {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT distinct * FROM users u " +
                    "WHERE NOT EXISTS(" +
                    "SELECT * FROM users_chats us WHERE us.username=u.username and us.chat_id=? ) ");
            preparedStatement.setInt(1, chatId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return usersExtractor.extract(resultSet);
        } catch (SQLException | ChatAppDatabaseException exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }
}
