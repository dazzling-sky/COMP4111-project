package LibraryManagementService_Async.Handlers;

import LibraryManagementService_Async.Operations.BookManagement;
import LibraryManagementService_Async.Operations.TransactionManagement;
import LibraryManagementService_Async.Utils.URIparser;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Class that handles all HTTP requests with PUT method
 */
public class PUTHandler extends Handler {

    /**
     * An instance of BookManagement Class that handles
     * tasks related to books
     * @see BookManagement
     */
    BookManagement bookMgmt = new BookManagement();

    /**
     * An instance of TransactionManagement Class that handles
     * tasks related to book transactions
     * @see TransactionManagement
     */
    TransactionManagement transMgmt = new TransactionManagement();


    /**
     * Method that handles all HTTP PUT requests
     *
     * @param request HTTP request sent by the client (librarian)
     * @param response HTTP response that needs to be returned back to the client (librarian)
     * @param context represents execution state of an HTTP process
     */
    @Override
    public void handleInternal(HttpRequest request, HttpResponse response, HttpContext context) {
        // Handle Put method only
        String raw_path = request.getRequestLine().getUri();

        if(URIparser.parsedUri(raw_path).equals("/BookManagementService/books")){
            bookMgmt.loanBooks(request, response);
        }

        else if(URIparser.parsedUri(raw_path).equals("/BookManagementService/transaction")){

            if(request.getRequestLine().getMethod().equals("POST")){
                String entityContent = "";
                try{ entityContent = EntityUtils.toString(((HttpEntityEnclosingRequest) request).getEntity());}
                catch(IOException e){ System.out.println(e);}
                if(entityContent.equals("")) {
                    transMgmt.requestTransactionId(request, response);
                }
                else{
                    transMgmt.commitOrCancel(request, response, entityContent);
                }
            }

            if(request.getRequestLine().getMethod().equals("PUT")){
                transMgmt.prepareOperations(request, response);
            }
        }
    }
}
