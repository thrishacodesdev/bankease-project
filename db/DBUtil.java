package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBUtil {

    private static final String URL="jdbc:mysql://localhost:3306/bank_system";
    private static final String USER="root";
    private static final String PASSWORD="Ullapo00@";
    public static Connection gConnection() throws SQLException
    {
return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
}
