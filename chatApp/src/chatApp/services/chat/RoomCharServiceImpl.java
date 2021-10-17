package chatApp.services.chat;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.exceptions.ChatUsersOverflowException;

public class RoomCharServiceImpl extends ChatServiceImpl{
    @Override
    public void addUser(User user, Chat chat) {
        chat.getUserList().add(user);
    }
}
