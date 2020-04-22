package LibraryManagementService_Async;

import LibraryManagementService_Async.Handlers.*;

import java.util.HashMap;
import java.util.Map;

public class Routes {
    private Map<String, Handler> patternForGet = new HashMap<>();
    private GETHandler getHandler = new GETHandler();

    private Map<String, Handler> patternForPost = new HashMap<>();
    private POSTHandler postHandler = new POSTHandler();

    private Map<String, Handler> patternForPut = new HashMap<>();
    private PUTHandler putHandler = new PUTHandler();

    private Map<String, Handler> patternForDelete = new HashMap<>();
    private DELETEHandler deleteHandler = new DELETEHandler();



    public Routes(){

        //Routes for Authentication functionalities
        patternForPost.put("/BookManagementService/login", postHandler);
        patternForGet.put("/BookManagementService/logout", getHandler);

        //Routes for booking operations functionalities
        patternForPost.put("/BookManagementService/books", postHandler);
        patternForGet.put("/BookManagementService/books" + "*", getHandler);
        patternForPut.put("/BookManagementService/books" + "*", putHandler);
        patternForDelete.put("/BookManagementService/books" + "*", deleteHandler);

        //Routes for transaction operations functionalities
        patternForPost.put("/BookManagementService/transaction", postHandler);
        patternForPut.put("/BookManagementService/transaction", putHandler);
    }

    public Map<String, Handler> getPatternForGet() {
        return patternForGet;
    }

    public Map<String, Handler> getPatternForPost() {
        return patternForPost;
    }

    public Map<String, Handler> getPatternForPut() {
        return patternForPut;
    }

    public Map<String, Handler> getPatternForDelete() {
        return patternForDelete;
    }
}
