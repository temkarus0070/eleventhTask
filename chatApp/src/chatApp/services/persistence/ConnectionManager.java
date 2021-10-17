package chatApp.services.persistence;
import java.sql.*;

public class ConnectionManager {
    private final static String DB_URL="jdbc:postgresql://localhost:5432/chat";

    private static ConnectionManager connectionManager;


    private ConnectionManager(){
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException classNotFoundException){
            classNotFoundException.printStackTrace();
        }
    }

    public static ConnectionManager getInstance(){
        if(connectionManager==null){
            connectionManager=new ConnectionManager();
        }
        return connectionManager;
    }

    public Connection getConnection(){
        try {
            return DriverManager.getConnection(DB_URL,"postgres","postgres");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


}
