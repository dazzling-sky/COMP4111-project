package LibraryManagementService_Async;

import LibraryManagementService_Async.Handlers.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that maps routes to handlers
 */
public class Routes {

    /**
     * HashMap property that maps each route to a GET handler
     */
    private Map<String, Handler> patternForGet = new HashMap<>();

    /**
     * An instance of a GETHandler Class
     */
    private GETHandler getHandler = new GETHandler();

    /**
     * HashMap property that maps each route to a POST handler
     */
    private Map<String, Handler> patternForPost = new HashMap<>();

    /**
     * An instance of a POSTHandler Class
     */
    private POSTHandler postHandler = new POSTHandler();

    /**
     * HashMap property that maps each route to a PUT handler
     */
    private Map<String, Handler> patternForPut = new HashMap<>();

    /**
     * An instance of a PUTHandler Class
     */
    private PUTHandler putHandler = new PUTHandler();

    /**
     * HashMap property that maps each route to a DELETE handler
     */
    private Map<String, Handler> patternForDelete = new HashMap<>();

    /**
     * An instance of a DELETEHandler Class
     */
    private DELETEHandler deleteHandler = new DELETEHandler();


    /**
     * Default Constructor of Routes Class
     */
    public Routes(){

        //Routes for Authentication functionalities
        patternForPost.put("/BookManagementService/login", postHandler);
        patternForGet.put("/BookManagementService/logout", getHandler);

        //Routes for booking operations functionalities
        patternForDelete.put("/BookManagementService/books" + "/*", deleteHandler);
        patternForGet.put("/BookManagementService/books", getHandler);
        patternForPost.put("/BookManagementService/books", postHandler);
        patternForPut.put("/BookManagementService/books" + "/*", putHandler);


        //Routes for transaction operations functionalities
        patternForPost.put("/BookManagementService/transaction", postHandler);
        patternForPut.put("/BookManagementService/transaction", putHandler);
    }

    /**
     * @return HashMap that maps each route to a GET handler
     */
    public Map<String, Handler> getPatternForGet() {
        return patternForGet;
    }

    /**
     * @return HashMap that maps each route to a POST handler
     */
    public Map<String, Handler> getPatternForPost() {
        return patternForPost;
    }

    /**
     * @return HashMap that maps each route to a PUT handler
     */
    public Map<String, Handler> getPatternForPut() {
        return patternForPut;
    }

    /**
     * @return HashMap that maps each route to a DELETE handler
     */
    public Map<String, Handler> getPatternForDelete() {
        return patternForDelete;
    }
}
