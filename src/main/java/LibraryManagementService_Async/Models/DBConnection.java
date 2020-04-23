package LibraryManagementService_Async.Models;

import java.sql.*;

public class DBConnection {
    private final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/library_management?autoReconnect=true&useSSL=false&allowMultiQueries=true";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "COMP4111mysql!";

    public ResultSet execQuery(String tableName, String condition){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = condition.equals("")?con.prepareStatement("select * from " + tableName) :
                    con.prepareStatement( "select * from " + tableName + " where " + condition + ";");
            return stmt.executeQuery();
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public void execUpdate(String tableName, String param, String condition){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = con.prepareStatement("update " + tableName + " set " + param + " where " +
                    condition + ";");
            stmt.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
