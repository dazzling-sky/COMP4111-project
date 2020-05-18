package LibraryManagementService_Async.Handlers;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.nio.protocol.*;
import org.apache.http.protocol.HttpContext;

/**
 * Abstract Class that implements HttpAsyncRequestHandler<HttpRequest> interface
 */
public abstract class Handler implements HttpAsyncRequestHandler<HttpRequest> {

    /**
     * @param request HTTP request sent by the client (librarian)
     * @param context represents execution state of an HTTP process
     * @return a consumer that buffers request content in memory
     */
    @Override
    public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context){
        // Buffer request content in memory for simplicity
        return new BasicAsyncRequestConsumer();
    }

    /**
     * @param request HTTP request sent by the client (librarian)
     * @param httpExchange encapsulates request received and a response to be generated in one exchange
     * @param context represents execution state of an HTTP process
     * @throws HttpException for communicating HTTP errors
     */
    @Override
    public void handle(HttpRequest request, HttpAsyncExchange httpExchange, HttpContext context) throws HttpException {
        final HttpResponse response = httpExchange.getResponse();
        handleInternal(request, response, context);
        httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
    }

    /**
     * Abstract method that needs to be handled by all Classes inheriting Handler Class
     *
     * @param request HTTP request sent by the client (librarian)
     * @param response HTTP response that needs to be returned back to the client (librarian)
     * @param context represents execution state of an HTTP process
     * @throws MethodNotSupportedException if method implemented is not supported
     */
    public abstract void handleInternal(HttpRequest request, HttpResponse response, HttpContext context) throws MethodNotSupportedException;
}
