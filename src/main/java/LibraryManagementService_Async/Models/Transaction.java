package LibraryManagementService_Async.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Transaction Class that stores all relevant properties and methods
 */
public class Transaction {

    /**
     * ID of a transaction instance within Transactions table
     */
    @JsonProperty("Transaction")
    private int transactionID;

    /**
     * ID of a book within Books table
     */
    @JsonProperty("Book")
    private int bookID;

    /**
     * List of actions that needs to be executed for a single transaction
     */
    @JsonProperty("Action")
    private String action;

    /**
     * @return ID of the Transaction instance
     */
    public int getTransactionID() {
        return transactionID;
    }

    /**
     * @return ID of the Book instance
     */
    public int getBookID() {
        return bookID;
    }

    /**
     * @return List of actions that needs to be executed
     */
    public String getAction() {
        return action;
    }

    /**
     * This method determines if there are any null fields for a Transaction instance
     *
     * @return false if there are no null fields, otherwise, true
     */
    public boolean containsNullField(){
        try{
            if(!(transactionID == 0) && !(bookID == 0) && !action.equals("null")){
                return false;
            }
        }catch(NullPointerException e){
            System.out.println(e);
            return true;
        }
        return true;
    }

    /**
     * This method checks if appropriate action has been given for a transaction
     *
     * @return false if there is an invalid action, otherwise, true
     */
    public boolean containsValidAction(){
        if(action.equals("return") || action.equals("loan")){
            return true;
        }

        return false;
    }
}

