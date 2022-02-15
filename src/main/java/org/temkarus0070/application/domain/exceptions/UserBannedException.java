package org.temkarus0070.application.domain.exceptions;

public class UserBannedException extends Exception{
    @Override
    public String getMessage() {
        return "You've banned at that chat";
    }
}
