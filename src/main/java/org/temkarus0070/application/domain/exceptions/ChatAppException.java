package org.temkarus0070.application.domain.exceptions;

public class ChatAppException extends Exception {
    public ChatAppException(Throwable throwable) {
        super(throwable);
    }

    public ChatAppException(String message) {
        super(message);
    }
}
