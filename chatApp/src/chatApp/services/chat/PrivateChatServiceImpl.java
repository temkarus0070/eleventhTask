package chatApp.services.chat;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.exceptions.ChatUsersOverflowException;

public class PrivateChatServiceImpl extends ChatServiceImpl {
    @Override
    public void addUser(User user, Chat chat) throws ChatUsersOverflowException {
        if(chat.getUserList().size()==2){
            throw new ChatUsersOverflowException();
        }
        else{
            chat.getUserList().add(user);
        }

    }
}
