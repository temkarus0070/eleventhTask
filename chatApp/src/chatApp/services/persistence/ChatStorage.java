package chatApp.services.persistence;

import chatApp.domain.chat.Chat;
import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.GroupChat;
import chatApp.domain.chat.RoomChat;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.mappers.ChatExtractor;
import chatApp.services.persistence.mappers.ChatsExtractor;
import chatApp.services.persistence.mappers.Extractor;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class ChatStorage implements ChatRepository {
    private String chatType = "";
    private final ConnectionManager connectionManager = ConnectionManager.getInstance();

    private Extractor<Chat> chatExtractor = ChatExtractor.getInstance();

    private ChatsExtractor chatsExtractor = ChatsExtractor.getInstance();

    public ChatStorage(ChatType chatType) {
        this.chatType = chatType.name();
    }


    @Override
    public Collection<Chat> get() throws SQLException {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM CHATS where chat_type=''%s''", chatType));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public Collection<Chat> getChatsByUser(String username) throws SQLException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format("" +
                    "SELECT * FROM users where name=?" +
                    "INNER JOIN ON users_chats ON name=?" +
                    "INNER JOIN ON Chats ON chatId=id" +
                    "WHERE chatType=%s", chatType));
            preparedStatement.setString(0, username);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return chatsExtractor.extract(resultSet);
        }

    }

    @Override
    public Optional<Chat> getChatByName(String name) throws SQLException {
        if (chatType.equals("PRIVATE"))
            throw new UnsupportedOperationException();
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chats where name=?");
            preparedStatement.setString(1, name);
            Chat chat = chatExtractor.extract(preparedStatement.executeQuery());
            return Optional.of(chat);
        }
    }

    @Override
    public void removeUserFromChat(String user, Integer chatId) throws Exception {
        try(Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement=connection.prepareStatement("DELETE FROM users_chats WHERE (username=? AND chat_id=?)");
            preparedStatement.setInt(2,chatId);
            preparedStatement.setString(1,user);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Chat get(Integer integer) throws SQLException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chats INNER JOIN users_chats" +
                    " on id=chat_id WHERE id=?");
            preparedStatement.setInt(1, integer);
            return chatExtractor.extract(preparedStatement.executeQuery());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public void add(Chat entity) throws SQLException {
        try (Connection connection = connectionManager.getConnection()) {
            ChatType chatType = entity.getType();
            PreparedStatement preparedStatement = null;
            String sql = "";
            switch (chatType) {
                case PRIVATE:
                    preparedStatement = connection.prepareStatement("INSERT INTO Chats(chat_type,owner) VALUES ('PRIVATE',?)", Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, entity.getChatOwner().getName());
                    break;
                case ROOM:
                    RoomChat roomChat = (RoomChat) entity;
                    preparedStatement = connection.prepareStatement("INSERT INTO Chats(chat_type,name,owner) VALUES ('PRIVATE',?,?)");
                    preparedStatement.setString(1, roomChat.getName());
                    preparedStatement.setString(2, roomChat.getChatOwner().getName());
                    break;
                case GROUP:
                    GroupChat groupChat = (GroupChat) entity;
                    preparedStatement = connection.prepareStatement("INSERT INTO chats(chat_type, name, users_count,owner) VALUES (" +
                            "'GROUP',?,?,?)");
                    preparedStatement.setString(1, groupChat.getName());
                    preparedStatement.setInt(2, groupChat.getUsersCount());
                    preparedStatement.setString(3, groupChat.getChatOwner().getName());

                    break;
            }
            if (preparedStatement != null) {
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                int id = resultSet.getInt(1);
                PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO users_chats VALUES (?,?,false)");
                preparedStatement1.setString(1, entity.getChatOwner().getName());
                preparedStatement1.setInt(2, entity.getId());
                preparedStatement1.executeUpdate();
                System.out.println(id);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }



    @Override
    public void delete(Integer integer) throws Exception {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement=connection.prepareStatement("DELETE FROM chats where id=?");
            preparedStatement.setInt(1,integer);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(Chat entity) throws SQLException {
        try(Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement=null;
            switch (entity.getType()){
                case ROOM:
                    RoomChat roomChat=(RoomChat)entity;
                    if(!((RoomChat)get(entity.getId())).getName().equals(((RoomChat) entity).getName())) {
                        preparedStatement = connection.prepareStatement("UPDATE chats SET name=? where id=?");
                        preparedStatement.setString(1, roomChat.getName());
                        preparedStatement.setInt(2,roomChat.getId());
                        preparedStatement.executeUpdate();
                    }
                    break;
                case GROUP:
                    GroupChat groupChat=(GroupChat) entity;
                    GroupChat existedChat=(GroupChat) get(entity.getId());
                    if(!(existedChat.getName().equals(groupChat.getName())&& existedChat.getUsersCount()==groupChat.getUsersCount())){
                        preparedStatement=connection.prepareStatement("UPDATE chats SET name=? and users_count=? where id=?");
                        preparedStatement.setString(1,groupChat.getName());
                        preparedStatement.setInt(2,groupChat.getUsersCount());
                        preparedStatement.setInt(3,groupChat.getId());
                        preparedStatement.executeUpdate();
                    }
                    break;
            }
        }
    }
}
