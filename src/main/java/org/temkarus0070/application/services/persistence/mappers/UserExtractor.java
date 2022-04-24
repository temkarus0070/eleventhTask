package org.temkarus0070.application.services.persistence.mappers;

import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserExtractor implements Extractor<User> {
    @Override
    public User extract(ResultSet resultSet) throws SQLException {
        User user=null;
        if(resultSet.next()){
            user = new User();
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setRoles(resultSet.getString("roles"));

        }
        return user;
    }
}
