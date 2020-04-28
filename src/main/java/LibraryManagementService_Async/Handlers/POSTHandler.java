package LibraryManagementService_Async.Handlers;

import LibraryManagementService_Async.Operations.Authentication;
import LibraryManagementService_Async.Operations.BookManagement;
import LibraryManagementService_Async.Utils.URIparser;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;

import java.util.Locale;

public class POSTHandler extends Handler{

    Authentication auth = new Authentication();
    BookManagement bookMgmt = new BookManagement();

    @Override
    public void handleInternal(HttpRequest request, HttpResponse response, HttpContext context) throws MethodNotSupportedException {
        // Handle Post method only
        String raw_path = request.getRequestLine().getUri();

        // Handle user login
        if(URIparser.parsedUri(raw_path).equals("/BookManagementService/login")){
            auth.handleLogin(request, response);
        }

        // Handle book add & lookup
        else if(URIparser.parsedUri(raw_path).equals("/BookManagementService/books")){
            if(request.getRequestLine().getMethod().equals("POST")){
                bookMgmt.addBooks(request, response);
            }
            if(request.getRequestLine().getMethod().equals("GET")){
                bookMgmt.lookBooks(request, response);
            }
        }
    }
}
