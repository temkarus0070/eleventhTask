package chatApp.services;

import chatApp.domain.User;
import chatApp.domain.exceptions.InvalidAuthDataException;
import chatApp.domain.exceptions.UsernameAlreadyExistException;

import javax.servlet.http.Cookie;

public interface AuthService {
    public boolean isAuthorized(Cookie[] cookies);
    public User login(String username, String password) throws InvalidAuthDataException;
    public User register(String username, String password) throws UsernameAlreadyExistException;
}
