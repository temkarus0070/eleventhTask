package chatApp.services;

import chatApp.domain.User;
import chatApp.domain.exceptions.InvalidAuthDataException;
import chatApp.domain.exceptions.UserNotExistsException;
import chatApp.domain.exceptions.UsernameAlreadyExistException;
import chatApp.services.persistence.implementation.PersistenceUserServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

public class AuthServiceImpl implements AuthService {
    private PersistenceUserService persistenceUserService;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(PersistenceUserService persistenceUserService,PasswordEncoder passwordEncoder){
        this.persistenceUserService=persistenceUserService;
        this.passwordEncoder=passwordEncoder;
    }
    @Override
    public boolean isAuthorized(Cookie[] cookies) {
        String username="";
        String password="";
        if(cookies!=null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                } else if (cookie.getName().equals("password")) {
                    password = cookie.getValue();
                }
            }
            try {
                login(username, password);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
        else
            return false;
    }



    @Override
    public User login(String username, String password) throws InvalidAuthDataException {
        User user=null;
        Optional<User> userOptional=persistenceUserService.getUser(username);
        if(userOptional.isPresent()){
            String hashPassword=passwordEncoder.getHashFromPassword(password);
            if(!(userOptional.get().getPassword().equals(hashPassword) ||
                    userOptional.get().getPassword().equals(password)))
                throw new InvalidAuthDataException();
        }
        return userOptional.get();
    }

    @Override
    public User register(String username, String password) throws UsernameAlreadyExistException {
        String hashPassword=passwordEncoder.getHashFromPassword(password);
        User user=new User(username,hashPassword);
        try {
            persistenceUserService.addUser(user);
        }
        catch (UsernameAlreadyExistException usernameAlreadyExistException){
            throw new UsernameAlreadyExistException();
        }
        return user;
    }

    @Override
    public User getCurrentUser(Cookie[] cookies)throws UserNotExistsException {
        Cookie cookie= Arrays.stream(cookies).filter(cookie1 -> cookie1.getName().equals("username")).findFirst().get();
        if(cookie!=null){
           return persistenceUserService.getUser(cookie.getValue()).get();
        }
        throw new UserNotExistsException();
    }
}
