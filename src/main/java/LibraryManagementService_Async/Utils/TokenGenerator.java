package LibraryManagementService_Async.Utils;

import LibraryManagementService_Async.Models.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "123456789";
    private static final DBConnection connection = new DBConnection();

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder;
        do{
            builder =  new StringBuilder();
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
        }while(!isLogin(builder.toString()));
        return builder.toString();


    }

    public static boolean isLogin(String token) {
        ResultSet rs1 = connection.execQuery("users", "*", String.format("access_token=\"%s\"", token));

        try {
            if (rs1.next()) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return true;
    }
}
