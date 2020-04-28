package LibraryManagementService_Async.Handlers;

import LibraryManagementService_Async.Operations.Authentication;

import LibraryManagementService_Async.Operations.BookManagement;
import LibraryManagementService_Async.Utils.URIparser;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;

import java.util.Locale;


public class GETHandler extends Handler {

    Authentication auth = new Authentication();
    BookManagement bookMgmt = new BookManagement();


    @Override
    public void handleInternal(HttpRequest request, HttpResponse response, HttpContext context) throws MethodNotSupportedException {

        String raw_path = request.getRequestLine().getUri();

        // Handle user logout
        if(URIparser.parsedUri(raw_path).equals("/BookManagementService/logout")){
            auth.handleLogout(request, response);
        }

        // Handle book lookup
        if(URIparser.parsedUri(raw_path).equals("/BookManagementService/books")){
            bookMgmt.lookBooks(request, response);
        }

    }
}
