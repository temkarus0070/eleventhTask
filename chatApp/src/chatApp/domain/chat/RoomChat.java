package chatApp.domain.chat;

public class RoomChat extends Chat {
    public RoomChat() {
        this.type = ChatType.ROOM;
    }

    private String name;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ChatType getType() {
        return super.getType();
    }
}
