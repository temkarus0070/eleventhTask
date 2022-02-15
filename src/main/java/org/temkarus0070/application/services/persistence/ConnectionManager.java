package org.temkarus0070.application.services.persistence;

import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.*;

public class ConnectionManager {
    private static Logger logger = Logger.getLogger(ConnectionManager.class.getName());
    private static ConnectionManager connectionManager;
    private static DataSource dataSource;

    private ConnectionManager() {
        try {
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:/comp/env/jdbc/chatDB");
        } catch (NamingException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
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
            logger.log(Level.SEVERE, exception.getMessage());
            throw new ChatAppDatabaseException(exception);
        }

    }


}
