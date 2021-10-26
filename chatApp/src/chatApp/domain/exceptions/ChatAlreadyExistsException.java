package chatApp.domain.exceptions;

public class ChatAlreadyExistsException extends Exception{
    @Override
    public String getMessage() {
        return "Chat with such data  exists";
    }
}
