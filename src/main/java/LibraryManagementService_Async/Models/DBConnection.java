package LibraryManagementService_Async.Models;

import java.sql.*;

/**
 * Class that handles all tasks related to reading/writing mysql database
 */
public class DBConnection {

    /**
     * String that correctly inputs all necessary parameters for mysql connection
     */
    private final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/library_management?autoReconnect=true&useSSL=false&allowMultiQueries=true";

    /**
     * Username for mysql database
     */
    private final String DB_USERNAME = "root";

    /**
     * Password for mysql database
     */
    private final String DB_PASSWORD = "COMP4111mysql!";

    /**
     * Method that allows execution of query within mysql database
     *
     * @param tableName name of the table that needs to be queried
     * @param selector column that needs to be selected for a query
     * @param condition condition that needs to be enforced for a query
     * @return ResultSet that contains a result of corresponding query
     * @throws ClassNotFoundException if appropriate input is not given for mysql Driver
     * @throws SQLException if wrong SQL statement is provided to the database
     */
    public ResultSet execQuery(String tableName, String selector,  String condition){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = condition.equals("")?con.prepareStatement("select " + selector + " from " + tableName + ";") :
                    con.prepareStatement( "select " + selector + " from " + tableName + " where " + condition + ";");
            return stmt.executeQuery();
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    /**
     * Method that allows execution of update within mysql database
     *
     * @param tableName name of the table that needs to be updated
     * @param param parameters that needs to be specifically updated
     * @param condition condition that needs to be enforced for an update
     * @throws ClassNotFoundException if appropriate input is not given for mysql Driver
     * @throws SQLException if wrong SQL statement is provided to the database
     */
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

    /**
     * Method that allows execution of insert within mysql database
     *
     * @param tableName name of the table where a row needs to be inserted
     * @param fields columns within the table where a rows needs to be inserted
     * @param values values that needs to be inserted into appropriate columns
     * @throws ClassNotFoundException if appropriate input is not given for mysql Driver
     * @throws SQLException if wrong SQL statement is provided to the database
     */
    public void execInsert(String tableName, String fields, String values){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = con.prepareStatement("insert into " + tableName + "(" + fields + ")" + " values " +
                    "(" + values + ");");
            stmt.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * Method that allows execution of delete within mysql database
     *
     * @param tableName name of the table where rows need to be deleted
     * @param condition condition that needs to be enforced for deletion of rows
     * @throws ClassNotFoundException if appropriate input is not given for mysql Driver
     * @throws SQLException if wrong SQL statement is provided to the database
     */
    public void execDelete(String tableName, String condition){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
            PreparedStatement stmt = con.prepareStatement("delete from " + tableName + " where " + condition + ";");
            stmt.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
