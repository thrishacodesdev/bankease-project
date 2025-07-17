package db;

import java.sql.Connection;
import java.sql.SQLException;
import db.DBUtil;

public class TestDB {
    public static void main(String[] args) {
        try{
            Connection con=DBUtil.gConnection();
            if(con!=null)
            {
                System.out.println("Database connected successfully!");
                con.close();
            }
            else{
                System.out.println("Failed to connect to the database");
            }
        }
        catch(SQLException e){
            System.out.println("ERROR:"+e.getMessage());

        }
    }
}
