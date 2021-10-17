package chatApp.services.persistence;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.GroupChat;
import chatApp.domain.chat.RoomChat;
import chatApp.services.persistence.interfaces.Repository;
import chatApp.services.persistence.mappers.ChatExtractor;
import chatApp.services.persistence.mappers.ChatsExtractor;
import chatApp.services.persistence.mappers.Extractor;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;

public class ChatStorage implements Repository<Chat,Integer> {
    private final ConnectionManager connectionManager=ConnectionManager.getInstance();

    private Extractor<Chat> chatExtractor = ChatExtractor.getInstance();

    private ChatsExtractor chatsExtractor=ChatsExtractor.getInstance();


    @Override
    public Collection<Chat> get() {
        try(Connection connection= connectionManager.getConnection();
            Statement statement= connection.createStatement();){
            ResultSet resultSet= statement.executeQuery("SELECT * FROM CHATS");
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return null;
    }

    public Collection<Chat> getChatsByUser(String username){
        try(Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement=connection.prepareStatement("" +
                    "SELECT * FROM users where name=?" +
                    "INNER JOIN ON users_chats ON name=?" +
                    "INNER JOIN ON Chats ON chatId=id");
            preparedStatement.setString(0,username);
            preparedStatement.setString(1,username);
           ResultSet resultSet= preparedStatement.executeQuery();
           return chatsExtractor.extract(resultSet);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;

    }

    @Override
    public Chat get(Integer integer) {
        try(Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM chats" +
                    "WHERE id=?");
            preparedStatement.setInt(0,integer);
            return chatExtractor.extract(preparedStatement.executeQuery());
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public void add(Chat entity) {
        try(Connection connection= connectionManager.getConnection()){
            ChatType chatType=entity.getType();
            PreparedStatement preparedStatement=null;
            String sql="";
            switch (chatType){
                case PRIVATE:
                    preparedStatement=connection.prepareStatement("INSERT INTO chat.Chats(id,chat_type) VALUES (?,'PRIVATE')");
                    preparedStatement.setInt(1,entity.getId());
                    break;
                case ROOM:
                    RoomChat roomChat=(RoomChat) entity;
                    preparedStatement=connection.prepareStatement("INSERT INTO chat.Chats(id,chat_type,name) VALUES (?,'PRIVATE',?)");
                    preparedStatement.setInt(1,roomChat.getId());
                    preparedStatement.setString(2,roomChat.getName());
                    break;
                case GROUP:
                    GroupChat groupChat=(GroupChat) entity;
                    preparedStatement=connection.prepareStatement("INSERT INTO Chat.chats(id, chat_type, name, users_count) VALUES (" +
                            "?,'GROUP',?,?)");
                    preparedStatement.setInt(1,groupChat.getId());
                    preparedStatement.setString(2,groupChat.getName());
                    preparedStatement.setInt(3,groupChat.getUsersCount());
                    break;
            }
            if(preparedStatement!=null)
                preparedStatement.executeUpdate();
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(Chat entity) {
        try(Connection connection= connectionManager.getConnection()){
            c
        }
        catch (Exception exception){
            exception.printStackTrace();
        }

    }

    @Override
    public void update(Chat entity) {

    }
}
