package LibraryManagementService_Async.Operations;

public class URIparser {

    public boolean containsParams(String raw_path){
        if (raw_path.indexOf("?") == -1){
            return false;
        }
        return true;
    }

    public String getNoParamsUri(String raw_path){
        return raw_path;
    }
}
