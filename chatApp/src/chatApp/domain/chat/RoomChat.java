package chatApp.domain.chat;

public class RoomChat extends Chat implements Nameable{
    private String name;

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }
}
