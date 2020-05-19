package LibraryManagementService_Async.Utils;

import LibraryManagementService_Async.Models.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that handles generation of unique token for each User instances
 */
public class TokenGenerator {

    /**
     * List of prefix alphabets that can be chosen
     */
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    /**
     * List of numbers that can be appended after the prefix
     */
    private static final String NUMBERS = "123456789";

    /**
     * An instance of DBConnection Class that handles all read/write operations within mysql database
     */
    private static final DBConnection connection = new DBConnection();

    /**
     * Method that creates a random sequence of unique tokens for each user
     *
     * @param count indicates total number of characters for the given token
     * @return string that represents unique, completed token
     */
    public static String randomAlphaNumeric(int count) {
        StringBuffer builder;
        do{
            builder =  new StringBuffer();
            while (count-- != 0) {
                if (count > 3){
                    int character = (int)(Math.random()*ALPHABET.length());
                    builder.append(ALPHABET.charAt(character));
                }
                else{
                    int number = (int)(Math.random()*NUMBERS.length());
                    builder.append(NUMBERS.charAt(number));
                }
            }
        }while(isLogin(builder.toString()));
        return builder.toString();


    }

    /**
     * Method that checks if a User instance is logged-in
     *
     * @param token indicates the unique token of a given user
     * @return true or false depending on the given User instance's status
     */
    public static boolean isLogin(String token) {
        ResultSet rs1 = connection.execQuery("users", "*", String.format("access_token=\"%s\"", token));

        try {
            if (rs1.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
}
