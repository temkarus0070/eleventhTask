package chatApp.services.persistence.mappers;

import chatApp.domain.User;
import chatApp.domain.chat.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                case ROOM:
                    RoomChat roomChat = new RoomChat();
                    roomChat.setId(resultSet.getInt("id"));
                    roomChat.setName(resultSet.getString("chat_name"));
                    chat=roomChat;
                case GROUP:
                    GroupChat groupChat = new GroupChat();
                    groupChat.setId(resultSet.getInt("id"));
                    groupChat.setUsersCount(resultSet.getInt("users_count"));
                    groupChat.setName(resultSet.getString("name"));
                    chat=groupChat;
            }
        }
        List<User> userList=new ArrayList<>();
        while (resultSet.next()){
            User user=new User(resultSet.getString("username"));
            userList.add(user);
        }
        chat.setUserList(userList);
        return chat;
    }
}
