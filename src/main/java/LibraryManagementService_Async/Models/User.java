package LibraryManagementService_Async.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User Class that stores all relevant properties and methods
 */
public class User {

    /**
     * Username for the user authentication
     */
    @JsonProperty("Username")
    private String Username;

    /**
     * Password for the user authentication
     */
    @JsonProperty("Password")
    private String Password;

    /**
     * Default constructor for the User Class
     */
    User(){}

    /**
     * @return Username of the user
     */
    public String getUsername(){
        return this.Username;
    }

    /**
     * @return Password of the user
     */
    public String getPassword(){
        return this.Password;
    }
}
