package LibraryManagementService_Async.Operations;

import LibraryManagementService_Async.Models.DBConnection;
import LibraryManagementService_Async.Models.User;
import LibraryManagementService_Async.Utils.*;

import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {

    private DBConnection connection = new DBConnection();

    public synchronized void handleLogin(HttpRequest request, HttpResponse response){
        HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
        User user = JSONConverter.convertToUser(entity);
        ResultSet rs1 = connection.execQuery("users", "*", String.format("Name=\"%s\" and Password=\"%s\" and Is_logon = b\'0\'", user.getUsername(), user.getPassword()));

        try{
            if (rs1.next()){
                String token = TokenGenerator.randomAlphaNumeric(7);
                String transactionID = TransactionIdGenerator.generateTransID();
                connection.execUpdate("users", String.format("Access_token=\"%s\",TransactionID=\"%s\", is_logon = b\'1\'", token, transactionID),
                            String.format("name=\"%s\" and password=\"%s\" and is_logon = b\'0\'", user.getUsername(), user.getPassword()));
                connection.execInsert("transactions", "Access_token, TransactionID", String.format("\"%s\", \"%s\"", token, transactionID));
                String payload = String.format("{ \"Token\" : \"%s\"}", token);
                StringEntity stringEntity = new StringEntity(payload, ContentType.APPLICATION_JSON);
                response.setEntity(stringEntity);
                response.setStatusCode(HttpStatus.SC_OK);
            }
            else{
                ResultSet rs2 = connection.execQuery("users", "*", String.format("name=\"%s\" and password=\"%s\"", user.getUsername(), user.getPassword()));
                if (rs2.next()){
                    response.setStatusCode(HttpStatus.SC_CONFLICT);
                }
                else{
                    response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                }
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    public synchronized void handleLogout(HttpRequest request, HttpResponse response){
        String token = URIparser.getToken(request.getRequestLine().getUri());
        ResultSet rs1 = connection.execQuery("users", "Is_logon", String.format("Access_token=\"%s\";", token));
        try{
            if(rs1.next()){
                int bit = rs1.getInt("Is_logon");
                if (bit == 1){
                    connection.execUpdate("users", "Access_token=NULL, TransactionID=NULL, is_logon=b\'0\'", String.format("Access_token=\"%s\"", token));
                    connection.execDelete("transactions", String.format("Access_token=\"%s\"", token));
                    Reminder.removeFromRunningReminders();
                    response.setStatusCode(HttpStatus.SC_OK);
                }
                else{
                    response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                }
            }
            else{
                response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }
}
