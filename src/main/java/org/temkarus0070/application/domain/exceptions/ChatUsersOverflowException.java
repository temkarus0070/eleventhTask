package org.temkarus0070.application.domain.exceptions;

public class ChatUsersOverflowException extends Exception{
    @Override
    public String getMessage() {
        return "Chat already has maximum users";
    }
}
