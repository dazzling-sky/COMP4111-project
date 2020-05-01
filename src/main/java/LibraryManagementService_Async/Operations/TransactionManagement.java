package LibraryManagementService_Async.Operations;

import LibraryManagementService_Async.Models.DBConnection;
import LibraryManagementService_Async.Models.Transaction;
import LibraryManagementService_Async.Utils.JSONConverter;
import LibraryManagementService_Async.Utils.TokenGenerator;
import LibraryManagementService_Async.Utils.URIparser;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TransactionManagement {
    DBConnection connection = new DBConnection();

    public void requestTransactionId(HttpRequest request, HttpResponse response){
        if(!TokenGenerator.isLogin(URIparser.getToken(request.getRequestLine().getUri()))){
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        else{
            String userToken = URIparser.getToken(request.getRequestLine().getUri());
            ResultSet rs1 = connection.execQuery("users", "TransactionID", String.format("Access_token=\"%s\"", userToken));
            try{
                if(rs1.next()){
                    String ids = rs1.getString("TransactionID");
                    if(ids == null){
                        response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                    }
                    else{
                        String[] idArray = ids.split(",");

                        if(idArray.length == 1){
                            String json = String.format("{\"Transaction\":%s}", idArray[0]);
                            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
                            response.setEntity(stringEntity);
                            response.setStatusCode(HttpStatus.SC_OK);
                        }
                        else{
                            StringBuffer jsonIdList = new StringBuffer();
                            jsonIdList.append("[");
                            for(String id: idArray){
                                jsonIdList.append(id + ", ");
                            }
                            String jsonIdListString = jsonIdList.toString();
                            jsonIdListString = jsonIdListString.trim().replaceAll(",$", "").concat("]");
                            String json = String.format("{\"Transaction\":%s}", jsonIdListString);
                            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
                            response.setEntity(stringEntity);
                            response.setStatusCode(HttpStatus.SC_OK);
                        }
                    }
                }
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }

    public void prepareOperations(HttpRequest request, HttpResponse response){
        if(!TokenGenerator.isLogin(URIparser.getToken(request.getRequestLine().getUri()))){
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        else{
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            Transaction transaction = JSONConverter.convertToTransaction(entity);
            StringBuffer actions = new StringBuffer();
            ResultSet rs1 = connection.execQuery("transactions", "Action", String.format("TransactionID=\"%s\";", transaction.getTransactionID()));
            try{
                if(rs1.next()){
                    String pastActions = rs1.getString("Action");
                    if(pastActions != null) {
                        actions.append(pastActions + ",");
                    }
                    actions.append(String.format("%s=%s", transaction.getBookID(), transaction.getAction().charAt(0)));

                    connection.execUpdate("transactions", String.format("Action=\"%s\"", actions.toString()), String.format("TransactionID=\"%s\"", transaction.getTransactionID()));

                    response.setStatusCode(HttpStatus.SC_OK);
                }
                else{
                    response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                }
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }
}
