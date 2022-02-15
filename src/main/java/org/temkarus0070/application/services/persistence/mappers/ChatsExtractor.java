package org.temkarus0070.application.services.persistence.mappers;

import org.temkarus0070.application.domain.chat.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatsExtractor implements Extractor<List<Chat>> {
    private static ChatsExtractor chatsExtractor;
    private ChatsExtractor(){

    }
    public static ChatsExtractor getInstance(){
        if(chatsExtractor ==null){
            chatsExtractor =new ChatsExtractor();
        }
        return chatsExtractor;
    }

    @Override
    public List<Chat> extract(ResultSet resultSet) throws SQLException {
        List<Chat> chatList=new ArrayList<>();
        while (resultSet.next()){
            ChatType chatType=ChatType.valueOf(resultSet.getString("chat_type"));
            switch (chatType){
                case PRIVATE:{
                    PrivateChat privateChat=new PrivateChat();
                    privateChat.setId(resultSet.getInt("id"));
                    chatList.add(privateChat);
                    break;
                }
                case ROOM:
                    RoomChat roomChat=new RoomChat();
                    roomChat.setName(resultSet.getString("name"));
                    roomChat.setId(resultSet.getInt("id"));
                    chatList.add(roomChat);
                    break;
                case GROUP:
                    GroupChat groupChat=new GroupChat();
                    groupChat.setName(resultSet.getString("name"));
                    groupChat.setId(resultSet.getInt("id"));
                    groupChat.setUsersCount(resultSet.getInt("users_count"));
                    chatList.add(groupChat);
                    break;
            }
        }
        return chatList;
    }
}
