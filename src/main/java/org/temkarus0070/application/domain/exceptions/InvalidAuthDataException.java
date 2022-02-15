package org.temkarus0070.application.domain.exceptions;

public class InvalidAuthDataException extends Exception {
    @Override
    public String getMessage() {
        return "You've entered invalid auth data";
    }
}
