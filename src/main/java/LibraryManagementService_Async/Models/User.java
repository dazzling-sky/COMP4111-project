package LibraryManagementService_Async.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("Username")
    private String Username;

    @JsonProperty("Password")
    private String Password;

    User(){}

    public String getUsername(){
        return this.Username;
    }

    public String getPassword(){
        return this.Password;
    }
}
