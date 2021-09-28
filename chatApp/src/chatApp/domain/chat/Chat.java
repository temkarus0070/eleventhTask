package chatApp.domain.chat;

import chatApp.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Chat {
    protected ChatType type;
    private int id;
    private List<User> userList=new ArrayList<>();
    private List<User> bannedUsers=new ArrayList<>();
    private  List<Message> messages=new ArrayList<>();


    public ChatType getType() {
        return type;
    }

    public List<User> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(List<User> bannedUsers) {
        this.bannedUsers = bannedUsers;
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
        return Objects.hash(id, userList, bannedUsers, messages);
    }

    public void setId(int id) {
        this.id = id;
    }
}
