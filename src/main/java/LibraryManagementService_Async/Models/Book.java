package LibraryManagementService_Async.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;

public class Book implements Comparable<Book>{

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Author")
    private String author;

    @JsonProperty("Publisher")
    private String publisher;

    @JsonProperty("Year")
    private int year;

    private int id;

    public Book(){};

    public Book(String title, String author, String publisher, int year, int id){
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.id = id;
    }

    @Override
    public int compareTo(Book b)
    {
        return this.id - b.id;
    }

    public static class TitleCompare implements Comparator<Book> {
        @Override
        public int compare(Book b1, Book b2) { return b1.getTitle().compareTo(b2.getTitle());}
    }

    public static class AuthorCompare implements Comparator<Book> {
        @Override
        public int compare(Book b1, Book b2) { return b1.getAuthor().compareTo(b2.getAuthor());}
    }

    public static class  YearCompare implements Comparator<Book> {
        @Override
        public int compare(Book b1, Book b2) { return b1.getYear() - b2.getYear();}
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
