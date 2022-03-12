package org.temkarus0070.application.services.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.services.persistence.interfaces.UserRepository;
import org.temkarus0070.application.services.persistence.mappers.UserExtractor;
import org.temkarus0070.application.services.persistence.mappers.UsersExtractor;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class UserStorage implements UserRepository {

    private Logger myLogger = Logger.getLogger(this.getClass().getName());


    private UsersExtractor usersExtractor;


    private UserExtractor userExtractor;


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserStorage(UsersExtractor usersExtractor, UserExtractor userExtractor, JdbcTemplate jdbcTemplate) {
        this.usersExtractor = usersExtractor;
        this.userExtractor = userExtractor;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> get() throws ChatAppDatabaseException {
        try {
            List<User> list = jdbcTemplate.query("SELECT * FROM users", rs -> {
                return usersExtractor.extract(rs);
            });
            return list;
        } catch (org.springframework.dao.DataAccessException | ChatAppDatabaseException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public User get(String s) throws ChatAppDatabaseException {
        try {
            return jdbcTemplate.query(con -> {
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM users where username=?");
                preparedStatement.setString(1, s);
                return preparedStatement;
            }, rs -> {
                return userExtractor.extract(rs);
            });
        } catch (org.springframework.dao.DataAccessException | ChatAppDatabaseException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void add(User entity) throws ChatAppDatabaseException {
        try {
            jdbcTemplate.execute(con -> {
                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO users(username,password) VALUES (?,?)");
                preparedStatement.setString(1, entity.getUsername());
                preparedStatement.setString(2, entity.getPassword());
                return preparedStatement;
            }, (PreparedStatementCallback<Object>) ps -> ps.executeUpdate());
        } catch (org.springframework.dao.DataAccessException | ChatAppDatabaseException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
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

        try {
            return jdbcTemplate.execute(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT distinct * FROM users u " +
                        "WHERE NOT EXISTS(" +
                        "SELECT * FROM users_chats us WHERE us.username=u.username and us.chat_id=? ) ");
                preparedStatement.setInt(1, chatId);
                return preparedStatement;
            }, (PreparedStatementCallback<List<User>>) ps -> usersExtractor.extract(ps.executeQuery()));
        } catch (org.springframework.dao.DataAccessException | ChatAppDatabaseException exception) {
            myLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }
    }
}
