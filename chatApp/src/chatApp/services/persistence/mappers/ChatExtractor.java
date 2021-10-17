package chatApp.services.persistence.mappers;

import chatApp.domain.chat.*;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    public Chat extract(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        ChatType chatType=ChatType.valueOf(resultSet.getString("chat_type"));
        switch (chatType){
            case PRIVATE:
                PrivateChat chat=new PrivateChat();
                chat.setId(resultSet.getInt("id"));
                return chat;
            case ROOM:
                RoomChat roomChat=new RoomChat();
                roomChat.setId(resultSet.getInt("id"));
                roomChat.setName(resultSet.getString("chat_name"));
                return roomChat;
            case GROUP:
                GroupChat groupChat=new GroupChat();
                groupChat.setId(resultSet.getInt("id"));
                groupChat.setUsersCount(resultSet.getInt("users_count"));
                groupChat.setName(resultSet.getString("name"));
                return groupChat;
            default:
                throw new ClassNotFoundException();
        }
    }
}
