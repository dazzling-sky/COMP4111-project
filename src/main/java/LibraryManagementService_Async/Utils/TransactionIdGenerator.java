package LibraryManagementService_Async.Utils;

import LibraryManagementService_Async.Models.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionIdGenerator {
    private static DBConnection connection = new DBConnection();
    private static final String NUMBERS = "0123456789";

    public static String generateTransID(){
         StringBuffer builder;
         builder = new StringBuffer();
         while(builder.length() != 4) {
             int index = (int) (Math.random() * NUMBERS.length());
             builder.append(NUMBERS.charAt(index));
         }
         return builder.toString();
    }
}
