package chatApp.services.chat;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.GroupChat;
import chatApp.domain.chat.Message;
import chatApp.domain.chat.PrivateChat;
import chatApp.domain.exceptions.ChatUsersOverflowException;
import chatApp.domain.exceptions.UserBannedException;

public class ChatServiceImpl<T extends Chat> implements ChatService {

    @Override
    public void sendMessage(Message message,Chat chat) throws UserBannedException{
        if(hasBan(message.getSender(), chat)){
            throw new UserBannedException();
        }
        chat.getMessages().add(message);
    }

    @Override
    public void banUser(User user,Chat chat) {
        chat.getBanUser().add(user);
    }

    @Override
    public void addUserToChat(User user,Chat chat) throws ChatUsersOverflowException{
        String chatType=chat.getClass().getSimpleName();
        switch (chatType){
            case "PrivateChat":
                if(chat.getUserList().size()==2){
                    throw new ChatUsersOverflowException();
                }
                else{
                    chat.getUserList().add(user);
                }
                break;
            case "GroupChat":
                GroupChat groupChat=(GroupChat) chat;
                if(groupChat.getUsersCount()>groupChat.getUserList().size()){
                    groupChat.getUserList().add(user);
                }
                else
                    throw new ChatUsersOverflowException();
                break;
            default:
                chat.getUserList().add(user);
        }
    }

    private boolean hasBan(User user,Chat chat){
        return chat.getUserList().contains(user);
    }
}
