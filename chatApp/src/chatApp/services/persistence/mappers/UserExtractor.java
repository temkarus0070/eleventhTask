package chatApp.services.persistence.mappers;

import chatApp.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserExtractor implements Extractor<User> {
    private static UserExtractor userExtractor;

    private UserExtractor(){

    }

    public static UserExtractor getInstance(){
        if(userExtractor==null){
            userExtractor=new UserExtractor();
        }
        return userExtractor;
    }
    @Override
    public User extract(ResultSet resultSet) throws SQLException {
        User user=null;
        if(resultSet.next()){
            user=new User();
            user.setName(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));

        }
        return user;
    }
}
