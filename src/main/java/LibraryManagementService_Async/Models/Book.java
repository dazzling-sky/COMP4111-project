package LibraryManagementService_Async.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Author")
    private String author;

    @JsonProperty("Publisher")
    private String publisher;

    @JsonProperty("Year")
    private int year;

    public Book(){};

    public Book(String title, String author, String publisher, int year){
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
    }

    public String getTitle(){
        return this.title;
    }

    public String getAuthor(){
        return this.author;
    }

    public String getPublisher(){
        return this.publisher;
    }

    public int getYear(){
        return this.year;
    }

}
