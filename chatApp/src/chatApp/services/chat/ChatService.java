package chatApp.services.chat;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.Message;
import chatApp.domain.exceptions.ChatUsersOverflowException;
import chatApp.domain.exceptions.MessageSenderNotFoundException;
import chatApp.domain.exceptions.UserBannedException;

public interface ChatService {
    public void sendMessage(Message message, Chat chat) throws UserBannedException, MessageSenderNotFoundException;

    public void banUser(User user, Chat chat);


    public void addUser(User user, Chat chat) throws ChatUsersOverflowException;
    public boolean hasPermissions(User user,Chat chat);
}
