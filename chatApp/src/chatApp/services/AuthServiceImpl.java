package chatApp.services;

import chatApp.domain.User;
import chatApp.domain.exceptions.InvalidAuthDataException;
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

    public AuthServiceImpl(){
        persistenceUserService=new PersistenceUserServiceImpl();
    }
    @Override
    public boolean isAuthorized(Cookie[] cookies) {
        String username="";
        String password="";
        for(Cookie cookie:cookies){
                if(cookie.getName().equals("username")){
                    username=cookie.getValue();
                }
                else if(cookie.getName().equals("password")){
                    password=cookie.getValue();
                }
        }
        try {
            login(username, password);
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }

    private String getHashFromPassword(String password){
        StringBuilder stringBuilder=new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                stringBuilder.append(Integer.toString(
                        (bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuilder.toString();
        }
        catch ( NoSuchAlgorithmException algNotFoundException){
            return "";
        }
    }

    @Override
    public User login(String username, String password) throws InvalidAuthDataException {
        User user=null;
        Optional<User> userOptional=persistenceUserService.getUser(username);
        if(userOptional.isPresent()){
            String hashPassword=getHashFromPassword(password);
            if(!(userOptional.get().getPassword().equals(hashPassword) ||
                    userOptional.get().getPassword().equals(password)))
                throw new InvalidAuthDataException();
        }
        return userOptional.get();
    }

    @Override
    public User register(String username, String password) throws UsernameAlreadyExistException {
        String hashPassword=getHashFromPassword(password);
        User user=new User(username,hashPassword);
        try {
            persistenceUserService.addUser(user);
        }
        catch (UsernameAlreadyExistException usernameAlreadyExistException){
            throw new UsernameAlreadyExistException();
        }
        return user;
    }
}
