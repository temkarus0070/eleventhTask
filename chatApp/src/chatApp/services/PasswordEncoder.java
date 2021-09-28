package chatApp.services;

public interface PasswordEncoder {
    public String getHashFromPassword(String password);
}
