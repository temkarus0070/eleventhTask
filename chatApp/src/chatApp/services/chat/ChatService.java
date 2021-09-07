package chatApp.services.chat;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.Message;
import chatApp.domain.exceptions.ChatUsersOverflowException;
import chatApp.domain.exceptions.UserBannedException;

public interface ChatService {
    public void sendMessage(Message message, Chat chat);

    public void banUser(User user,Chat chat);


    public void addUserToChat(User user,Chat chat) throws ChatUsersOverflowException, UserBannedException;
}
