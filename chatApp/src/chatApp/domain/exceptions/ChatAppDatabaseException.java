package chatApp.domain.exceptions;

public class ChatAppDatabaseException extends RuntimeException {
    public ChatAppDatabaseException(Throwable throwable) {
        super(throwable);
    }

    public ChatAppDatabaseException(String message) {
        super(message);
    }
}
