package chatApp.services.chat;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.GroupChat;
import chatApp.domain.exceptions.ChatUsersOverflowException;

public class GroupChatServiceImpl extends ChatServiceImpl{
    @Override
    public void addUser(User user, Chat chat) throws ChatUsersOverflowException {
        GroupChat groupChat=(GroupChat) chat;
        if(groupChat.getUsersCount()>groupChat.getUserList().size()){
            groupChat.getUserList().add(user);
        }
        else
            throw new ChatUsersOverflowException();
    }
}
