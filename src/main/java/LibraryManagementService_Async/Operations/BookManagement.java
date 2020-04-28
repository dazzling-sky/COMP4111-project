package LibraryManagementService_Async.Operations;

import LibraryManagementService_Async.Models.Book;
import LibraryManagementService_Async.Models.DBConnection;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class BookManagement {

    private DBConnection connection = new DBConnection();

    public void addBooks(HttpRequest request, HttpResponse response){
        if(!TokenGenerator.isLogin(URIparser.getToken(request.getRequestLine().getUri()))){
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        else{
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            Book book = JSONConverter.convertToBook(entity);
            ResultSet rs1 = connection.execQuery("books", "*", String.format("Title=\"%s\";", book.getTitle()));

            try{
                if(!rs1.next()){
                    connection.execInsert(
                            "books",
                            "Title, Author, Publisher, Year, Available",
                             String.format("\"%s\", \"%s\", \"%s\", %d, b\'1\'", book.getTitle(), book.getAuthor(), book.getPublisher(), book.getYear())
                    );
                    ResultSet rs2 = connection.execQuery("books", "ID", String.format("Title=\"%s\";", book.getTitle()));
                    if(rs2.next()){
                        int id = rs2.getInt("ID");
                        String raw_path = request.getRequestLine().getUri();
                        String message = String.format("Please visit %s", "http://localhost:8080" +
                                URIparser.parsedUri(raw_path) + "/" +
                                id + "?" +
                                URIparser.getQueryParams(raw_path));
                        StringEntity stringEntity = new StringEntity(message, ContentType.DEFAULT_TEXT);
                        response.addHeader("Location", String.format("/books/%d", id));
                        response.setEntity(stringEntity);
                        response.setStatusCode(HttpStatus.SC_CREATED);
                    }
                }
                else{
                    ResultSet rs3 = connection.execQuery("books", "ID", String.format("Title=\"%s\";", book.getTitle()));
                    rs3.next();
                    int id = rs3.getInt("ID");
                    response.addHeader("Duplicate record", String.format("/books/%d", id));
                    response.setStatusCode(HttpStatus.SC_CONFLICT);
                }
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }

    public void lookBooks(HttpRequest request, HttpResponse response){
        if(!TokenGenerator.isLogin(URIparser.getToken(request.getRequestLine().getUri()))){
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        else{
            Map<String, String> paramsMap = URIparser.getQueryParams(request.getRequestLine().getUri());
            StringBuffer condition = new StringBuffer();
            int limit = 0;
            for (String key: paramsMap.keySet()){
                if(key.equals("id")){
                    condition.append(String.format("ID=%s or ", paramsMap.get(key))); continue;
                }
                if(key.equals("title")){
                    condition.append(String.format("Title like \"%%" + "%s" + "%%\" or ", paramsMap.get(key))); continue;
                }
                if(key.equals("author")){
                    condition.append(String.format("Author like \"%%" +"%s" + "%%\" or ", paramsMap.get(key))); continue;
                }
                if(key.equals("limit")){
                    limit = Integer.parseInt(paramsMap.get(key));
                }
            }

            String mod_condition;
            if(condition.length() > 0){
                int index = condition.lastIndexOf("or");
                mod_condition = condition.substring(0, index - 1);
                mod_condition.concat(";");
            }else{
                mod_condition = "";
            }

            ResultSet rs2 = connection.execQuery("books", "*", mod_condition);

            List<Book> bookList = new ArrayList<>();
            try{
                if(rs2.next()) {
                    do {
                        int id = rs2.getInt("ID");
                        String title = rs2.getString("Title");
                        String author = rs2.getString("Author");
                        String publisher = rs2.getString("Publisher");
                        int year = rs2.getInt("Year");
                        Book book = new Book(title, author, publisher, year, id);
                        bookList.add(book);
                    } while (rs2.next());

                    List<Book> sortedBookList = sortBooks(bookList, paramsMap);

                    if (sortedBookList.size() < limit){
                        limit = sortedBookList.size();
                    }
                    String json = JSONConverter.produceBookListContent(bookList, limit);

                    if(limit == 0){limit = sortedBookList.size();}
                    String entity = String.format("{ \"FoundBooks\":%d, \"Results\":%s}", limit, json);
                    StringEntity stringEntity = new StringEntity(entity, ContentType.APPLICATION_JSON);
                    response.setEntity(stringEntity);
                    response.setStatusCode(HttpStatus.SC_OK);
                }
                else{
                    response.setStatusCode(HttpStatus.SC_NO_CONTENT);
                }
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }

    public List<Book> sortBooks(List<Book> bookList, Map<String, String> paramsMap){
        String sortBy = "id"; String order = "asc";
        for(String key: paramsMap.keySet()){
           if(key.equals("sortby")){
               sortBy = paramsMap.get(key); continue;
           }
           if(key.equals("order")){
               order = paramsMap.get(key); continue;
           }
        }

        switch(sortBy){
            case "id":
                if(order.equals("asc")){ Collections.sort(bookList);}else{Collections.sort(bookList, Collections.reverseOrder());}
                break;
            case "title":
                if(order.equals("asc")){Collections.sort(bookList, new Book.TitleCompare());}else{Collections.sort(bookList, Collections.reverseOrder(new Book.TitleCompare()));}
                break;
            case "author":
                if(order.equals("asc")){Collections.sort(bookList, new Book.AuthorCompare());}else{Collections.sort(bookList, Collections.reverseOrder(new Book.AuthorCompare()));}
                break;
            case "year":
                if(order.equals("asc")){Collections.sort(bookList, new Book.YearCompare());}else{Collections.sort(bookList, Collections.reverseOrder(new Book.YearCompare()));}
            default:
        }
        return bookList;
    }

    public void loanBooks(HttpRequest request, HttpResponse response){
        if(!TokenGenerator.isLogin(URIparser.getToken(request.getRequestLine().getUri()))){
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        else{
            int id = URIparser.getBookId(request.getRequestLine().getUri());
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            StringBuffer entityContent = new StringBuffer();
            try{
                entityContent.append(EntityUtils.toString(entity));
            }catch(IOException e){
                System.out.println(e);
            }
            int valueIndex = entityContent.indexOf(":");
            int closingIndex = entityContent.indexOf("}");
            String available = entityContent.substring(valueIndex + 1, closingIndex).trim();

            if(available.equals("false")){
                ResultSet rs1 = connection.execQuery("books", "*", String.format("ID=%d;", id));
                try{
                    if(rs1.next()){
                        int bit = rs1.getInt("Available");
                        if(bit == 1){
                            connection.execUpdate("books", "Available=0", String.format("ID=%d;", id));
                            response.setStatusCode(HttpStatus.SC_OK);
                        }
                        else{
                            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                        }
                    }
                    else{
                        response.setStatusLine(HttpVersion.HTTP_1_1, 404, "No book record");
                    }
                }catch(SQLException e){
                    System.out.println(e);
                }
            }

            if(available.equals("true")){
                ResultSet rs1 = connection.execQuery("books", "*", String.format("ID=%d;", id));
                try {
                    if (rs1.next()) {
                        int bit = rs1.getInt("Available");
                        if(bit == 0){
                            connection.execUpdate("books", "Available=1", String.format("ID=%d;", id));
                            response.setStatusCode(HttpStatus.SC_OK);
                        }
                        else{
                            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                        }
                    }
                    else{
                        response.setStatusLine(HttpVersion.HTTP_1_1, 404, "No book record");
                    }
                }catch(SQLException e){
                    System.out.println(e);
                }
            }
        }
    }
}
