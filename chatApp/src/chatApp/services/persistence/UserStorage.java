package chatApp.services.persistence;

import chatApp.domain.User;
import chatApp.services.persistence.interfaces.UserRepository;
import chatApp.services.persistence.mappers.UserExtractor;
import chatApp.services.persistence.mappers.UsersExtractor;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;

public class UserStorage implements UserRepository {
    private ConnectionManager connectionManager;
    private UsersExtractor usersExtractor=UsersExtractor.getInstance();
    private UserExtractor userExtractor=UserExtractor.getInstance();


    public UserStorage(){
        connectionManager=ConnectionManager.getInstance();
    }
    @Override
    public Collection<User> get()throws SQLException {
        try(Connection connection= connectionManager.getConnection()){
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM users");
            return usersExtractor.extract(resultSet);
        }
    }

    @Override
    public User get(String s)throws SQLException {
        try(Connection connection= connectionManager.getConnection()) {
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM users where username=?");
            preparedStatement.setString(1,s);
            return userExtractor.extract(preparedStatement.executeQuery());
        }
    }

    @Override
    public void add(User entity)throws SQLException {
        try(Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO users(username,password) VALUES (?,?)");
            preparedStatement.setString(1,entity.getName());
            preparedStatement.setString(2,entity.getPassword());
            preparedStatement.executeUpdate();
        }
    }



    @Override
    public void delete(String s) throws Exception {

    }

    @Override
    public void update(User entity)throws SQLException {

    }

    @Override
    public Collection<User> getUsersNotAtThatChat(Integer chatId) throws Exception{
        try(Connection connection=ConnectionManager.getInstance().getConnection()){
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT distinct * FROM users u " +
                    "WHERE NOT EXISTS(" +
                    "SELECT * FROM users_chats us WHERE us.username=u.username and us.chat_id=? ) ");
            preparedStatement.setInt(1,chatId);
            ResultSet resultSet=preparedStatement.executeQuery();
            return usersExtractor.extract(resultSet);
        }
    }
}
