package org.temkarus0070.application.domain.exceptions;

public class UserNotExistsException extends Exception{

    @Override
    public String getMessage() {
        return "That user not exists";
    }
}
