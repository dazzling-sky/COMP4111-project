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

public class PUTHandler extends Handler {
    BookManagement bookMgmt = new BookManagement();
    TransactionManagement transMgmt = new TransactionManagement();

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
            }
        }
    }
}
