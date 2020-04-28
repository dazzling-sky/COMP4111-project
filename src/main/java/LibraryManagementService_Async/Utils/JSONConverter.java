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
import java.util.List;

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

    public static String produceBookListContent(List<Book> bookList, int limit){
        StringBuffer array = new StringBuffer(); array.append("[");
        try{
            ObjectMapper mapper = new ObjectMapper();
            if(limit == 0){
                for(int i=0; i<bookList.size(); i++){
                    String json = mapper.writeValueAsString(bookList.get(i));

                    if(i == bookList.size() - 1){
                        array.append(json).append("]");
                    }
                    else{
                        array.append(json).append(",");
                    }
                }
            }
            else{
                for(int i=0; i<limit; i++){
                    String json = mapper.writeValueAsString(bookList.get(i));

                    if(i == bookList.size() - 1){
                        array.append(json).append("]");
                    }
                    else{
                        array.append(json).append(",");
                    }
                }
            }
        }catch(JsonMappingException e){
            System.out.println(e);
        }catch(JsonGenerationException e){
            System.out.println(e);
        }catch(JsonProcessingException e){
            System.out.println(e);
        }
        return array.toString();
    }
}
