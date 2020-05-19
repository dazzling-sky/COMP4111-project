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
}

