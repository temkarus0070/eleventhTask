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
        User user=new User();
        if(resultSet.next()){
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));

        }
        return user;
    }
}
