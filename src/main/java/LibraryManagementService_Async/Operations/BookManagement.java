package LibraryManagementService_Async.Operations;

import LibraryManagementService_Async.Models.Book;
import LibraryManagementService_Async.Models.DBConnection;

import LibraryManagementService_Async.Utils.JSONConverter;
import LibraryManagementService_Async.Utils.TokenGenerator;
import LibraryManagementService_Async.Utils.URIparser;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.sql.ResultSet;
import java.sql.SQLException;


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
}
