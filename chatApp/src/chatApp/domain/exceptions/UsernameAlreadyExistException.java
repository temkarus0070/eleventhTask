package chatApp.domain.exceptions;

public class UsernameAlreadyExistException extends Exception{
    @Override
    public String getMessage() {
        return "Your username already have used";
    }
}
