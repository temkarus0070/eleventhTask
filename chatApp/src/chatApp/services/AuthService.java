package chatApp.services;

import chatApp.domain.User;
import chatApp.domain.exceptions.ChatAppException;
import chatApp.domain.exceptions.InvalidAuthDataException;
import chatApp.domain.exceptions.UserNotExistsException;
import chatApp.domain.exceptions.UsernameAlreadyExistException;

import javax.servlet.http.Cookie;

public interface AuthService {
    public boolean isAuthorized(Cookie[] cookies) throws ChatAppException;

    public boolean login(String username, String password) throws ChatAppException;

    public boolean register(String username, String password) throws ChatAppException;

    public User getCurrentUser(Cookie[] cookies) throws ChatAppException;
}
