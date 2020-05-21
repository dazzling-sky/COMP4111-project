package LibraryManagementService_Async.Handlers;

import LibraryManagementService_Async.Operations.Authentication;
import LibraryManagementService_Async.Operations.BookManagement;
import LibraryManagementService_Async.Utils.URIparser;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpContext;

/**
 * Class that handles all HTTP requests with GET method
 */
public class GETHandler extends Handler {

    /**
     * An instance of Authentication Class that handles
     * tasks related to user login/logout
     *
     * @see Authentication
     */
    Authentication auth = new Authentication();

    /**
     * An instance of BookManagement Class that handles
     * tasks related to books
     *
     * @see BookManagement
     */
    BookManagement bookMgmt = new BookManagement();

    /**
     * Method that handles all GET HTTP requests
     *
     * @param request HTTP request sent by the client (librarian)
     * @param response HTTP response that needs to be returned back to the client (librarian)
     * @param context represents execution state of an HTTP process
     */
    @Override
    public void handleInternal(HttpRequest request, HttpResponse response, HttpContext context)  {
        // Handle Post method only
        String raw_path = request.getRequestLine().getUri();

        // Handle user logout
        if(URIparser.parsedUri(raw_path).equals("/BookManagementService/logout")){
            auth.handleLogout(request, response);
        }

        // Handle book lookup
        else if(URIparser.parsedUri(raw_path).equals("/BookManagementService/books")){
            bookMgmt.lookBooks(request, response);
        }

        else{
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
    }
}
