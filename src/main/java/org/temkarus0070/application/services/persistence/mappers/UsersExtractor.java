package org.temkarus0070.application.services.persistence.mappers;

import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UsersExtractor implements Extractor<List<User>> {
    @Override
    public List<User> extract(ResultSet resultSet) throws SQLException {
        List<User> userList=new ArrayList<>();
        while (resultSet.next()){
            User user=new User();
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            userList.add(user);
        }
        return userList;
    }
}
