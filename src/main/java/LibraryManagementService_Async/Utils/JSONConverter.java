package LibraryManagementService_Async.Utils;

import LibraryManagementService_Async.Models.Book;
import LibraryManagementService_Async.Models.User;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class JSONConverter {

    public static User convertToUser(HttpEntity entity){
        try{
            String entityContent = EntityUtils.toString(entity);
            ObjectMapper mapper = new ObjectMapper();
            try{
                User user =  mapper.readValue(entityContent, User.class);
                return user;
            }catch(JsonMappingException e){
                System.out.println(e);
            }catch(JsonGenerationException e){
                System.out.println(e);
            }catch(JsonProcessingException e){
                System.out.println(e);
            }
        }catch(IOException e){
            System.out.println(e);
        }
        return null;
    }

    public static Book convertToBook(HttpEntity entity){
        try{
            String entityContent = EntityUtils.toString(entity);
            ObjectMapper mapper = new ObjectMapper();
            try{
                Book book = mapper.readValue(entityContent, Book.class);
                return book;
            }catch(JsonMappingException e){
                System.out.println(e);
            }catch(JsonGenerationException e){
                System.out.println(e);
            }catch(JsonProcessingException e){
                System.out.println(e);
            }
        }catch(IOException e){
            System.out.println(e);
        }
        return null;
    }
}
