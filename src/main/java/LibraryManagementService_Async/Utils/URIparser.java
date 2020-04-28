package LibraryManagementService_Async.Utils;

import java.util.HashMap;
import java.util.Map;

public class URIparser {

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

    public static int getBookId(String raw_path){
        String url = raw_path.substring(0, raw_path.indexOf("?"));
        int index_put = url.lastIndexOf("/");
        return Integer.parseInt(url.substring(index_put + 1));
    }

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

    public static String getToken(String raw_path){
        Map<String, String> paramsMap = getQueryParams(raw_path);
        return paramsMap.get("token");
    }
}
