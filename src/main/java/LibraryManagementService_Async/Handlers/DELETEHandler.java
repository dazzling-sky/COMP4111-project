package LibraryManagementService_Async.Handlers;

import LibraryManagementService_Async.Operations.BookManagement;
import LibraryManagementService_Async.Utils.URIparser;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

/**
 * Class that handles all HTTP requests with DELETE method
 */
public class DELETEHandler extends Handler{

    /**
     * An instance of BookManagement Class that handles
     * tasks related to books
     *
     * @see BookManagement
     */
    private BookManagement bookMgmt = new BookManagement();

    /**
     * Method that handles all DELETE HTTP requests
     *
     * @param request HTTP request sent by the client (librarian)
     * @param response HTTP response that needs to be returned back to the client (librarian)
     * @param context represents execution state of an HTTP process
     */
    @Override
    public void handleInternal(HttpRequest request, HttpResponse response, HttpContext context) {
        // Handle Delete method only
        String raw_path = request.getRequestLine().getUri();

        if(URIparser.parsedUri(raw_path).equals("/BookManagementService/books")){
           if(request.getRequestLine().getMethod().equals("PUT")){
               bookMgmt.loanBooks(request, response);
           }
           if(request.getRequestLine().getMethod().equals("DELETE")){
               bookMgmt.deleteBooks(request, response);
           }
        }
    }
}
