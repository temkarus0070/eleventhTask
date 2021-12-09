package chatApp.services.persistence;

import chatApp.domain.exceptions.ChatAppDatabaseException;

import javax.sql.DataSource;
import java.sql.*;
import javax.naming.*;

public class ConnectionManager {
    private static ConnectionManager connectionManager;
    private static DataSource dataSource;

    private ConnectionManager() {
        try {
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:/comp/env/jdbc/chatDB");
        } catch (NamingException ex) {
            throw new ChatAppDatabaseException(ex);
        }
    }

    public static Connection getConnection() throws ChatAppDatabaseException {
        if (connectionManager == null)
            connectionManager = new ConnectionManager();
        try {
            Connection connection = dataSource.getConnection();
            return connection;
        } catch (SQLException exception) {
            throw new ChatAppDatabaseException(exception);
        }

    }


}
