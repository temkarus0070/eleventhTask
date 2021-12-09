package chatApp.services.persistence;

import chatApp.MyLogger;
import chatApp.domain.exceptions.ChatAppDatabaseException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.*;

public class ConnectionManager {
    private static ConnectionManager connectionManager;
    private static DataSource dataSource;

    private ConnectionManager() {
        try {
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:/comp/env/jdbc/chatDB");
        } catch (NamingException ex) {
            MyLogger.log(Level.SEVERE, ex.getMessage());
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
            MyLogger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }

    }


}
