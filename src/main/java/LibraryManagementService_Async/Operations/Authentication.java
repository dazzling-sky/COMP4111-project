package LibraryManagementService_Async.Operations;

import LibraryManagementService_Async.Models.DBConnection;
import LibraryManagementService_Async.Models.User;
import LibraryManagementService_Async.Utils.JSONConverter;
import LibraryManagementService_Async.Utils.TokenGenerator;

import LibraryManagementService_Async.Utils.URIparser;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {

    private DBConnection connection = new DBConnection();

    public void handleLogin(HttpRequest request, HttpResponse response){
        HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
        User user = JSONConverter.convertToUser(entity);
        ResultSet rs1 = connection.execQuery("users", "*", String.format("name=\"%s\" and password=\"%s\" and is_logon = b\'0\'", user.getUsername(), user.getPassword()));

        try{
            if (rs1.next()){
                String token = TokenGenerator.randomAlphaNumeric(7);
                connection.execUpdate("users", String.format("access_token=\"%s\", is_logon = b\'1\'", token),
                        String.format("name=\"%s\" and password=\"%s\" and is_logon = b\'0\'", user.getUsername(), user.getPassword()));
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

    public void handleLogout(HttpRequest request, HttpResponse response){
        String token = URIparser.getToken(request.getRequestLine().getUri());
        ResultSet rs1 = connection.execQuery("users", "is_logon", String.format("access_token=\"%s\";", token));
        try{
            if(rs1.next()){
                int bit = rs1.getInt("is_logon");
                if (bit == 1){
                    connection.execUpdate("users", "access_token=NULL, is_logon=b\'0\'", String.format("access_token=\"%s\"", token));
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
