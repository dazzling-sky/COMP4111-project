package LibraryManagementService_Async.Handlers;

import LibraryManagementService_Async.Operations.BookManagement;
import LibraryManagementService_Async.Utils.URIparser;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class DELETEHandler extends Handler{
    BookManagement bookMgmt = new BookManagement();

    @Override
    public void handleInternal(HttpRequest request, HttpResponse response, HttpContext context) {
        // Handle Delete method only
        String raw_path = request.getRequestLine().getUri();

        if(URIparser.parsedUri(raw_path).equals("/BookManagementService/books")){
           if(request.getRequestLine().getMethod().equals("PUT")){
               bookMgmt.loanBooks(request, response);
           }
           if(request.getRequestLine().getMethod().equals("DELETE")){
           }
        }
    }
}
