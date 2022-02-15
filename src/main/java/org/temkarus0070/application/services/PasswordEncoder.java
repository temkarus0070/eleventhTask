package org.temkarus0070.application.services;

public interface PasswordEncoder {
    public String getHashFromPassword(String password);
}
