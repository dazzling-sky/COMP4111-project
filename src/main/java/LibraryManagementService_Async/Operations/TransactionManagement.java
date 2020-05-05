package LibraryManagementService_Async.Operations;

import LibraryManagementService_Async.Models.DBConnection;
import LibraryManagementService_Async.Models.Transaction;
import LibraryManagementService_Async.Utils.JSONConverter;
import LibraryManagementService_Async.Utils.Reminder;
import LibraryManagementService_Async.Utils.TokenGenerator;
import LibraryManagementService_Async.Utils.URIparser;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
                    if(!Reminder.exists(String.valueOf(transaction.getTransactionID()))){
                        Reminder reminder = new Reminder(String.valueOf(transaction.getTransactionID()));
                        Reminder.addReminders(reminder);
                        reminder.start(20);
                    }
                    else{
                        Reminder reminder = Reminder.getInstance(String.valueOf(transaction.getTransactionID()));
                        Reminder.CheckOperationTask task = reminder.getTask();
                        System.out.println(task.hasRunStarted());
                        if(!task.hasRunStarted()){
                            reminder.stop();
                        }
                        reminder.start(20);
                        task.setHasStarted(false);
                    }

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

    public void commitOrCancel(HttpRequest request, HttpResponse response, String entityContent){
        if(!TokenGenerator.isLogin(URIparser.getToken(request.getRequestLine().getUri()))){
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        else{
            String action = JSONConverter.getAction(entityContent);
            String userToken = URIparser.getToken(request.getRequestLine().getUri());

            if(action.equals("\"commit\"")){
                ResultSet rs1 = connection.execQuery("books", "ID,Available", "");
                Map<Integer, Boolean> bookAvailability = new HashMap<>();
                try{
                    while(rs1.next()){
                        int bookID = rs1.getInt("ID");
                        int available = rs1.getInt("Available");
                        if (available == 0){
                            bookAvailability.put(bookID, false);
                        }
                        else{
                            bookAvailability.put(bookID, true);
                        }
                    }
                }catch(SQLException e){
                    System.out.println(e);
                }

                ResultSet rs2 = connection.execQuery("transactions", "Action,TransactionID", String.format("Access_token=\"%s\";", userToken));
                try{
                    if(rs2.next()){
                        String actions = rs2.getString("Action");
                        String transactionID = rs2.getString("TransactionID");
                        if(actions == null){
                            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                        }
                        else{
                            String[] idActionPair = actions.split(",");
                            Map<Boolean, Map<Integer,Boolean>> result = isValidTransaction(bookAvailability, idActionPair);
                            if (result.containsKey(true)){

                                for (Integer key: result.get(true).keySet()){
                                    if (result.get(true).get(key) == false){
                                        connection.execUpdate("books", "Available=b\'0\'", String.format("ID=\"%s\";", key));
                                    }
                                    else{
                                        connection.execUpdate("books", "Available=b\'1\'", String.format("ID=\"%s\";", key));
                                    }
                                }
                                connection.execUpdate("transactions", "Action=null", String.format("Access_token=\"%s\"", userToken));
                                if(Reminder.exists(transactionID)){
                                    Reminder reminder = Reminder.getInstance(transactionID);
                                    Reminder.CheckOperationTask task = reminder.getTask();
                                    task.setHasStarted(true);
                                    reminder.stop();
                                }
                                response.setStatusCode(HttpStatus.SC_OK);
                            }
                            else{
                                response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                            }
                        }
                    }
                    else{
                        response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                    }
                }catch(SQLException e){
                    System.out.println(e);
                }
            }
            else if (action.equals("\"cancel\"")){
                ResultSet rs3 = connection.execQuery("transactions", "Action,TransactionID", String.format("Access_token=\"%s\";", userToken));
                try {
                    if (rs3.next()) {
                        String actions = rs3.getString("Action");
                        String transactionID = rs3.getString("TransactionID");
                        if (actions != null) {
                            connection.execUpdate("transactions", "Action=null", String.format("Access_token=\"%s\"", userToken));
                            if(Reminder.exists(transactionID)){
                                Reminder reminder = Reminder.getInstance(transactionID);
                                Reminder.CheckOperationTask task = reminder.getTask();
                                task.setHasStarted(true);
                                reminder.stop();
                            }
                            response.setStatusCode(HttpStatus.SC_OK);
                        } else {
                            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                        }
                    } else {
                        response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                    }
                }catch(SQLException e){
                    System.out.println(e);
                }
            }
            else{
                response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            }
        }
    }

    public Map<Boolean, Map<Integer,Boolean>> isValidTransaction(Map<Integer, Boolean> bookAvailability, String[] idActionPair){

        Map<Boolean, Map<Integer, Boolean>> result = new HashMap<>();

        for (int i=0; i<idActionPair.length; i++){
            String id = idActionPair[i].split("=")[0];
            String action = idActionPair[i].split("=")[1];

            boolean isAvailable = bookAvailability.get(Integer.parseInt(id));

            if (bookAvailability.get(Integer.parseInt(id)) == null){
                result.put(false, null);
                return result;
            }

            if (isAvailable == false && action.equals("l")){
                result.put(false, null);
                return result;
            }

            if(isAvailable == true && action.equals("r")){
                result.put(false, null);
                return result;
            }

            if(isAvailable == false && action.equals("r")){
                bookAvailability.replace(Integer.parseInt(id), true);
                continue;
            }

            if(isAvailable == true && action.equals("l")){
                bookAvailability.replace(Integer.parseInt(id), false);
                continue;
            }
        }
        result.put(true, bookAvailability);
        return result;
    }
}
