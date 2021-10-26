package chatApp.domain.exceptions;

public class UserNotExistsException extends Exception{

    @Override
    public String getMessage() {
        return "That user not exists";
    }
}
