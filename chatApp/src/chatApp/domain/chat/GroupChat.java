package chatApp.domain.chat;

public class GroupChat extends Chat implements Nameable{
private String name;
private int usersCount;

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
