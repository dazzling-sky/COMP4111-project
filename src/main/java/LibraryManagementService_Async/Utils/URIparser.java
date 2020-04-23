package LibraryManagementService_Async.Utils;

public class URIparser {

    public static boolean containsParams(String raw_path){
        if (raw_path.indexOf("?") == -1){
            return false;
        }
        return true;
    }

    public static String getNoParamsUri(String raw_path){
        return raw_path;
    }
}
