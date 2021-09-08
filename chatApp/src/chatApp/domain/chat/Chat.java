package chatApp.domain.chat;

import chatApp.domain.User;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return id == chat.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userList, banUser, messages);
    }

    public void setId(int id) {
        this.id = id;
    }
}
