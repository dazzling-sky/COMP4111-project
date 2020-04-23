package LibraryManagementService_Async.Handlers;

import LibraryManagementService_Async.Operations.Authentication;
import LibraryManagementService_Async.Utils.URIparser;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;

import java.util.Locale;

public class POSTHandler extends Handler{

    Authentication auth = new Authentication();

    @Override
    public void handleInternal(HttpRequest request, HttpResponse response, HttpContext context) throws MethodNotSupportedException {
        // Handle Post method only
        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
        if(!method.equals("POST")){
            throw new MethodNotSupportedException("Only " + method + "method supported");
        }

        String raw_path = request.getRequestLine().getUri();

        // Handle no parameter requests
        if (!URIparser.containsParams(raw_path)){
            if(URIparser.getNoParamsUri(raw_path).equals("/BookManagementService/login")){
                auth.handleLogin(request, response);
            }
        }
    }
}
