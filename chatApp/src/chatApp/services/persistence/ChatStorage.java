package chatApp.services.persistence;

import chatApp.domain.chat.*;
import chatApp.domain.exceptions.ChatAppDatabaseException;
import chatApp.domain.exceptions.ChatAppException;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.mappers.ChatExtractor;
import chatApp.services.persistence.mappers.ChatsExtractor;
import chatApp.services.persistence.mappers.Extractor;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class ChatStorage implements ChatRepository {
    private final ChatType chatType;
    private final Array array;
    private final ConnectionManager connectionManager;

    private final Extractor<Chat> chatExtractor;

    private final ChatsExtractor chatsExtractor;

    public ChatStorage(ChatType chatType) throws ChatAppDatabaseException {
        chatExtractor = ChatExtractor.getInstance();
        chatsExtractor = ChatsExtractor.getInstance();
        this.chatType = chatType;
        this.connectionManager = new ConnectionManager();
        try (Connection connection = connectionManager.getConnection()) {
            String[] array = chatType.getValues();
            this.array = connection.createArrayOf("text", array);
        } catch (SQLException sqlException) {
            throw new ChatAppDatabaseException(sqlException);
        }


    }

    @Override
    public Collection<Chat> get() throws ChatAppDatabaseException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement statement = null;
            statement = connection.prepareStatement("SELECT * FROM CHATS where chat_type::text in ? ORDER BY id");
            statement.setArray(1, array);
            ResultSet resultSet = statement.executeQuery();
            return chatsExtractor.extract(resultSet);
        } catch (SQLException sqlException) {
            throw new ChatAppDatabaseException(sqlException);
        }
    }

    public Collection<Chat> getChatsByUser(String username) throws ChatAppDatabaseException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement("SELECT * FROM users u " +
                    "INNER JOIN  users_chats uc ON u.username=uc.username " +
                    "INNER JOIN  Chats c ON c.id=uc.chat_id " +
                    "WHERE u.username=? and c.chat_type::text  = any(?::text[]) " +
                    "ORDER BY c.id");

            preparedStatement.setArray(2, array);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            return chatsExtractor.extract(resultSet);
        } catch (SQLException throwables) {
            throw new ChatAppDatabaseException(throwables);
        }

    }

    @Override
    public Optional<Chat> getChatByName(String name) throws ChatAppDatabaseException {
        if (chatType == ChatType.PRIVATE)
            throw new UnsupportedOperationException();
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement("SELECT * FROM chats as c INNER JOIN users_chats as usChats" +
                    " on c.id=usChats.chat_id " +
                    "LEFT JOIN  messages as m on (m.chat_id=c.id and m.sender_name=usChats.username) " +
                    " WHERE c.name=? and c.chat_type::text = any(?::text[])");
            preparedStatement.setArray(2, array);

            Chat chat = chatExtractor.extract(preparedStatement.executeQuery());
            return Optional.ofNullable(chat);
        } catch (SQLException exception) {
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void removeUserFromChat(String user, Integer chatId) throws ChatAppDatabaseException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users_chats WHERE (username=? AND chat_id=?)");
            preparedStatement.setInt(2, chatId);
            preparedStatement.setString(1, user);
            preparedStatement.executeUpdate();
        } catch (SQLException chatAppDatabaseException) {
            throw new ChatAppDatabaseException(chatAppDatabaseException);
        }
    }

    @Override
    public void addUserToChat(String user, Integer chatId) throws ChatAppDatabaseException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users_chats(username,chat_id,has_ban) values(?,?,false)");
            preparedStatement.setString(1, user);
            preparedStatement.setInt(2, chatId);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void banUserInChat(String user, Integer chatId) throws ChatAppDatabaseException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users_chats SET has_ban=true " +
                    "where (username=? AND chat_id=?)");
            preparedStatement.setString(1, user);
            preparedStatement.setInt(2, chatId);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void addMessage(Message message, Integer chatId) throws ChatAppDatabaseException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages(text,sender_name,chat_id) values (?,?,?)");
            preparedStatement.setString(1, message.getContent());
            preparedStatement.setString(2, message.getSender().getName());
            preparedStatement.setInt(3, chatId);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public Chat get(Integer integer) throws ChatAppDatabaseException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chats as c INNER JOIN users_chats as usChats" +
                    " on c.id=usChats.chat_id " +
                    "LEFT JOIN  messages as m on (m.chat_id=c.id and m.sender_name=usChats.username) " +
                    " WHERE c.id=? AND c.chat_type::text = any(?::text[])");
            preparedStatement.setArray(2, array);
            preparedStatement.setInt(1, integer);

            return chatExtractor.extract(preparedStatement.executeQuery());
        } catch (SQLException exception) {
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void add(Chat entity) throws ChatAppDatabaseException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = null;
            switch (chatType) {
                case PRIVATE:
                    preparedStatement = connection.prepareStatement("INSERT INTO Chats(chat_type,owner) VALUES ('PRIVATE',?)", Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, entity.getChatOwner().getName());
                    break;
                case ROOM:
                    RoomChat roomChat = (RoomChat) entity;
                    preparedStatement = connection.prepareStatement("INSERT INTO Chats(chat_type,name,owner) VALUES ('ROOM',?,?)", Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, roomChat.getName());
                    preparedStatement.setString(2, roomChat.getChatOwner().getName());
                    break;
                case GROUP:
                    GroupChat groupChat = (GroupChat) entity;
                    preparedStatement = connection.prepareStatement("INSERT INTO chats(chat_type, name, users_count,owner) VALUES (" +
                            "'GROUP',?,?,?)", Statement.RETURN_GENERATED_KEYS);
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
                entity.setId(id);
                PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO users_chats VALUES (?,?,false)");
                preparedStatement1.setString(1, entity.getChatOwner().getName());
                preparedStatement1.setInt(2, entity.getId());
                preparedStatement1.executeUpdate();
                System.out.println(id);
            }
        } catch (SQLException exception) {
            throw new ChatAppDatabaseException(exception);
        }
    }


    @Override
    public void delete(Integer integer) throws ChatAppDatabaseException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM chats where id=?");
            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new ChatAppDatabaseException(exception);
        }
    }

    @Override
    public void update(Chat entity) throws ChatAppDatabaseException {
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = null;
            switch (entity.getType()) {
                case ROOM:
                    RoomChat roomChat = (RoomChat) entity;
                    if (!((RoomChat) get(entity.getId())).getName().equals(((RoomChat) entity).getName())) {
                        preparedStatement = connection.prepareStatement("UPDATE chats SET name=? where id=?");
                        preparedStatement.setString(1, roomChat.getName());
                        preparedStatement.setInt(2, roomChat.getId());
                        preparedStatement.executeUpdate();
                    }
                    break;
                case GROUP:
                    GroupChat groupChat = (GroupChat) entity;
                    GroupChat existedChat = (GroupChat) get(entity.getId());
                    if (!(existedChat.getName().equals(groupChat.getName()) && existedChat.getUsersCount() == groupChat.getUsersCount())) {
                        preparedStatement = connection.prepareStatement("UPDATE chats SET name=? and users_count=? where id=?");
                        preparedStatement.setString(1, groupChat.getName());
                        preparedStatement.setInt(2, groupChat.getUsersCount());
                        preparedStatement.setInt(3, groupChat.getId());
                        preparedStatement.executeUpdate();
                    }
                    break;
            }
        } catch (SQLException exception) {
            throw new ChatAppDatabaseException(exception);
        }
    }
}
