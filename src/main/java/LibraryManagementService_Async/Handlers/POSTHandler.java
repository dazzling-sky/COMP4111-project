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
        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
        if(!method.equals("POST")){
            throw new MethodNotSupportedException("Only " + method + "method supported");
        }

        String raw_path = request.getRequestLine().getUri();

        // Handle user login
        if(URIparser.parsedUri(raw_path).equals("/BookManagementService/login")){
            auth.handleLogin(request, response);
        }

        // Handle book add
        if(URIparser.parsedUri(raw_path).equals("/BookManagementService/books")){
            bookMgmt.addBooks(request, response);
        }
    }
}