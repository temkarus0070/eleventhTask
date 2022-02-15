package org.temkarus0070.application.services;

import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.exceptions.ChatAppException;

import javax.servlet.http.Cookie;


public interface AuthService {
    public boolean isAuthorized(Cookie[] cookies) throws ChatAppException;

    public boolean login(String username, String password) throws ChatAppException;

    public boolean register(String username, String password) throws ChatAppException;

    public User getCurrentUser(Cookie[] cookies) throws ChatAppException;
}
