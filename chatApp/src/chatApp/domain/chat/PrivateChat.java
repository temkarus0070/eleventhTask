package chatApp.domain.chat;

public class PrivateChat extends Chat {

    public PrivateChat() {
        this.type = ChatType.PRIVATE;
    }

    @Override
    public ChatType getType() {
        return super.getType();
    }

    @Override
    public String toString() {
        return getType().name() + " " + getId();
    }
}
