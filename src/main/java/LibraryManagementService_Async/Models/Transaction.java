package LibraryManagementService_Async.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {
    @JsonProperty("Transaction")
    private int transactionID;

    @JsonProperty("Book")
    private int bookID;

    @JsonProperty("Action")
    private String action;

    public int getTransactionID() {
        return transactionID;
    }

    public int getBookID() {
        return bookID;
    }

    public String getAction() {
        return action;
    }
}

