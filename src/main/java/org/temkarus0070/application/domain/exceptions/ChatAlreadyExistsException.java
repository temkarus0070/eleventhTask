package org.temkarus0070.application.domain.exceptions;

public class ChatAlreadyExistsException extends Exception{
    @Override
    public String getMessage() {
        return "Chat with such data  exists";
    }
}
