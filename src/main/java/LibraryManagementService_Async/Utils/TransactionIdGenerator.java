package LibraryManagementService_Async.Utils;

import LibraryManagementService_Async.Models.DBConnection;

/**
 * Class that generates Transaction ID for a given User instance
 */
public class TransactionIdGenerator {

    /**
     * An instance of DBConnection Class that handles all read/write operations within mysql database
     */
    private static DBConnection connection = new DBConnection();

    /**
     * List of numbers that can be appended after the prefix
     */
    private static final String NUMBERS = "0123456789";

    /**
     * Method that generates 4 digit transaction ID for a given User Interface
     *
     * @return string that represents transaction id
     */
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
