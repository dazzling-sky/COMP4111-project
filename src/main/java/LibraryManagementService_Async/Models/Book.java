package LibraryManagementService_Async.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;

/**
 * Book Class that stores all relevant properties and methods
 * <p>
 * It implements Comparable<T> interface to compare each book's id
 */
public class Book implements Comparable<Book>{

    /**
     * Title of the book
     */
    @JsonProperty("Title")
    private String title;

    /**
     * Author of the book
     */
    @JsonProperty("Author")
    private String author;

    /**
     * Publisher of the book
     */
    @JsonProperty("Publisher")
    private String publisher;

    /**
     * Year the book was published
     */
    @JsonProperty("Year")
    private int year;

    /**
     * ID of the book within mysql database
     */
    private int id;

    /**
     * User-defined constructor for Book Class
     *
     * @param title indicates title of the book
     * @param author indicates author of the book
     * @param publisher indicates publisher of the book
     * @param year indicates year the book was published
     * @param id indicates id of the book within mysql database
     */
    public Book(String title, String author, String publisher, int year, int id){
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.id = id;
    }

    /**
     * This method allows comparison of books using id properties
     *
     * @param b another book that is compared
     * @return 1,0 or -1 depending on two book's relative id values
     */
    @Override
    public int compareTo(Book b)
    {
        return this.id - b.id;
    }

    /**
     * Static Inner Class that implements Comparator<T> interface to compare books using title values
     */
    public static class TitleCompare implements Comparator<Book> {

        /**
         * This method allows comparison of books using title properties
         * <p>
         * Note that Title needs to be unique according to the project specification
         *
         * @param b1 first book that needs to be compared
         * @param b2 second book that needs to be compared
         * @return 1,0 or -1 depending on two book's relative title values
         */
        @Override
        public int compare(Book b1, Book b2) { return b1.getTitle().compareTo(b2.getTitle());}
    }

    /**
     * Static Inner Class that implements Comparator<T> interface to compare books using author values
     */
    public static class AuthorCompare implements Comparator<Book> {

        /**
         * This method allows comparison of books using author properties
         *
         * @param b1 first book that needs to be compared
         * @param b2 second book that needs to be compared
         * @return 1,0 or -1 depending on two book's relative author values
         */
        @Override
        public int compare(Book b1, Book b2) { return b1.getAuthor().compareTo(b2.getAuthor());}
    }

    /**
     * Static Inner Class that implements Comparator<T> interface to compare books using the year they were published
     */
    public static class  YearCompare implements Comparator<Book> {

        /**
         * This method allows comparison of books using year properties
         *
         * @param b1 first book that needs to be compared
         * @param b2 second book that needs to be compared
         * @return 1,0 or -1 depending on two book's relative year values
         */
        @Override
        public int compare(Book b1, Book b2) { return b1.getYear() - b2.getYear();}
    }

    /**
     * @return title of the book
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * @return author of the book
     */
    public String getAuthor(){
        return this.author;
    }

    /**
     * @return publisher of the book
     */
    public String getPublisher(){
        return this.publisher;
    }

    /**
     * @return the year book was published
     */
    public int getYear(){
        return this.year;
    }

}
