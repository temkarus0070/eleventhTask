package chatApp.domain.exceptions;

public class UserBannedException extends Exception{
    @Override
    public String getMessage() {
        return "You've banned at that chat";
    }
}
