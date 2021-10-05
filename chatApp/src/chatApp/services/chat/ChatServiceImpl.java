package chatApp.services.chat;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.GroupChat;
import chatApp.domain.chat.Message;
import chatApp.domain.exceptions.ChatUsersOverflowException;
import chatApp.domain.exceptions.MessageSenderNotFoundException;
import chatApp.domain.exceptions.UserBannedException;

public abstract class ChatServiceImpl implements ChatService {


    @Override
    public void sendMessage(Message message, Chat chat) throws UserBannedException, MessageSenderNotFoundException {
        if (hasBan(message.getSender(), chat)) {
            throw new UserBannedException();
        }
        if (message.getSender() == null) //переместить наверх
            throw new MessageSenderNotFoundException();
        chat.getMessages().add(message);
    }


    @Override
    public abstract void addUser(User user, Chat chat) throws ChatUsersOverflowException, UserBannedException;

    @Override
    public void banUser(User user, Chat chat) {
        chat.getBannedUsers().add(user);
    }


    private boolean hasBan(User user, Chat chat) {
        return chat.getUserList().contains(user);
    } //в utils
}
