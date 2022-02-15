package org.temkarus0070.application.domain.exceptions;

public class UsernameAlreadyExistException extends Exception{
    @Override
    public String getMessage() {
        return "Your username already have used";
    }
}
