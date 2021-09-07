package chatApp.domain.chat;

import chatApp.domain.User;

import java.util.List;

public abstract class Chat {
    private int id;
    private List<User> userList;
    private List<User> banUser;
    private  List<Message> messages;

    public List<User> getBanUser() {
        return banUser;
    }

    public void setBanUser(List<User> banUser) {
        this.banUser = banUser;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
