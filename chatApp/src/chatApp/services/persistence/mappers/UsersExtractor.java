package chatApp.services.persistence.mappers;

import chatApp.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsersExtractor implements Extractor<List<User>> {
    private static UsersExtractor usersExtractor;
    private UsersExtractor(){

    }

    public static UsersExtractor getInstance(){
        if(usersExtractor==null){
            usersExtractor=new UsersExtractor();
        }
        return usersExtractor;
    }
    @Override
    public List<User> extract(ResultSet resultSet) throws SQLException {
        List<User> userList=new ArrayList<>();
        while (resultSet.next()){
            User user=new User();
            user.setName(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            userList.add(user);
        }
        return userList;
    }
}
