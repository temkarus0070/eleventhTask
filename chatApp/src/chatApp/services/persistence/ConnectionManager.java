package chatApp.services.persistence;
import chatApp.domain.exceptions.ChatAppDatabaseException;

import javax.sql.DataSource;
import java.sql.*;
import javax.naming.*;

public class ConnectionManager {

    public Connection getConnection() throws ChatAppDatabaseException {
        try {
            InitialContext initialContext = new InitialContext();
            DataSource ds = (DataSource) initialContext.lookup("java:/comp/env/jdbc/chatDB");
            return ds.getConnection();
        } catch (SQLException | NamingException ex) {
            throw new ChatAppDatabaseException(ex);
        }
    }


}
