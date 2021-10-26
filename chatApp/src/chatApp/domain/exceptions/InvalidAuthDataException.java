package chatApp.domain.exceptions;

public class InvalidAuthDataException extends Exception {
    @Override
    public String getMessage() {
        return "You've entered invalid auth data";
    }
}
