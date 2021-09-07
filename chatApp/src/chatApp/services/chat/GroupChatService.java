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
        checkBan(message.getSender(), chat);
        chat.getMessages().add(message);
    }

    @Override
    public void banUser(User user,Chat chat) {
        chat.get
    }

    @Override
    public void addUserToChat(User user,Chat chat) throws ChatUsersOverflowException, UserBannedException {
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

    private void checkBan(User user,Chat chat)throws UserBannedException {
        if(chat.getUserList().contains(user)){
            throw new UserBannedException();
        }
    }
}
