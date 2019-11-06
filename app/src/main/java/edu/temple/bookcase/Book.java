package edu.temple.bookcase;

public class Book {

    int id;             // A system provided ID for the book
    String title;       // The title of the book
    String author;      // The author of the book
    int published;      // The year the book was published
    String coverURL;    // A URL to the image representing the book cover

    public Book (int id, String title, String author, int published, String coverURL) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.published = published;
        this.coverURL = coverURL;
    }
}
