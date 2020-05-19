package LibraryManagementService_Async.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that parses HTTP request URI
 */
public class URIparser {

    /**
     * Method that generates parsed URI from a given path
     *
     * @param raw_path indicates original URI submitted during HTTP request
     * @return a string that represents URI until the beginning of input parameters
     */
    public static String parsedUri(String raw_path){
        if (raw_path.indexOf("?") == -1){
            return raw_path;
        }
        else{
            int index = raw_path.indexOf("?");
            String url = raw_path.substring(0, index);

            int index_put = url.lastIndexOf("/");
            if(url.charAt(index_put + 1) < '0' || url.charAt(index_put + 1) > '9'){
                return url;
            }
            return url.substring(0, index_put);
        }
    }

    /**
     * Method that retrieves book id from a raw path
     *
     * @param raw_path indicates original URI submitted during HTTP request
     * @return an integer that represents corresponding book ID
     */
    public static int getBookId(String raw_path){
        String url = raw_path.substring(0, raw_path.indexOf("?"));
        int index_put = url.lastIndexOf("/");
        return Integer.parseInt(url.substring(index_put + 1));
    }

    /**
     * Method that retrieves query parameters from a raw path
     *
     * @param raw_path indicates original URI submitted during HTTP request
     * @return a Map that contains key-value pair for each query parameter
     */
    public static Map<String, String> getQueryParams(String raw_path){
        String queryParams = raw_path.substring(raw_path.indexOf("?")+1);
        Map<String, String> paramsMap = new HashMap<>();

        if (queryParams.contains("&")){
            String[] paramsArray = queryParams.split("&");
            for(int i=0; i<paramsArray.length; i++){
                paramsMap.put(paramsArray[i].split("=")[0], paramsArray[i].split("=")[1]);
            }
        }
        else{
            paramsMap.put(queryParams.split("=")[0], queryParams.split("=")[1]);
        }
        return paramsMap;
    }

    /**
     * Method that retrieves a unique User instance token from a raw path
     *
     * @param raw_path indicates original URI submitted during HTTP request
     * @return a string that represents a unique token of a User instance
     */
    public static String getToken(String raw_path){
        Map<String, String> paramsMap = getQueryParams(raw_path);
        return paramsMap.get("token");
    }
}
