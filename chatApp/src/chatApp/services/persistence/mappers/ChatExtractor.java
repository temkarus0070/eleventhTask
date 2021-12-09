package chatApp.services.persistence.mappers;

import chatApp.domain.User;
import chatApp.domain.chat.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatExtractor implements Extractor<Chat>{
    private static ChatExtractor chatExtractor;
    private ChatExtractor(){

    }

    public static ChatExtractor getInstance(){
        if(chatExtractor ==null){
            chatExtractor =new ChatExtractor();
        }
        return chatExtractor;
    }



    @Override
    public Chat extract(ResultSet resultSet) throws SQLException {
        Chat chat=null;
        if (resultSet.next()) {
            ChatType chatType = ChatType.valueOf(resultSet.getString("chat_type"));
            switch (chatType) {
                case PRIVATE:
                    chat = new PrivateChat();
                    chat.setId(resultSet.getInt("id"));
                    break;
                case ROOM:
                    RoomChat roomChat = new RoomChat();
                    roomChat.setId(resultSet.getInt("id"));
                    roomChat.setName(resultSet.getString("name"));
                    chat = roomChat;
                    break;
                case GROUP:
                    GroupChat groupChat = new GroupChat();
                    groupChat.setId(resultSet.getInt("id"));
                    groupChat.setUsersCount(resultSet.getInt("users_count"));
                    groupChat.setName(resultSet.getString("name"));
                    chat = groupChat;
                    break;
            }

            List<User> bannnedUsers = new ArrayList<>();
            List<User> userList = new ArrayList<>();
            List<Message> messageList = new ArrayList<>();
            Set<User> userSet = new HashSet<>();
            do {
                User user = new User(resultSet.getString("username"));
                if (!userSet.contains(user)) {
                    if (resultSet.getBoolean("has_ban")) {
                        bannnedUsers.add(user);
                    }
                    userList.add(user);
                    userSet.add(user);
                }
                Message message = new Message(resultSet.getString("text"), new User(resultSet.getString("sender_name")));
                messageList.add(message);
            }
            while (resultSet.next());
            chat.setBannedUsers(bannnedUsers);
            chat.setUserList(userList);
            chat.setMessages(messageList);
        }
        return chat;
    }
}
