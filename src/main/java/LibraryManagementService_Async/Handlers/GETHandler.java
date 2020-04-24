package LibraryManagementService_Async.Handlers;

import LibraryManagementService_Async.Operations.Authentication;

import LibraryManagementService_Async.Utils.URIparser;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;

import java.util.Locale;


public class GETHandler extends Handler {

    Authentication auth = new Authentication();


    @Override
    public void handleInternal(HttpRequest request, HttpResponse response, HttpContext context) throws MethodNotSupportedException {
        //Handle GET method only
        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
        if(!method.equals("GET")){
            throw new MethodNotSupportedException("Only " + method + "method supported");
        }

        String raw_path = request.getRequestLine().getUri();

        if(URIparser.parsedUri(raw_path).equals("/BookManagementService/logout")){
            auth.handleLogout(request, response);
        }

    }
}
