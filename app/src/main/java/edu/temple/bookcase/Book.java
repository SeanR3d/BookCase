package edu.temple.bookcase;

import java.io.Serializable;

public class Book implements Serializable {

    int id;             // A system provided ID for the book
    String title;       // The title of the book
    String author;      // The author of the book
    int published;      // The year the book was published
    String coverURL;    // A URL to the image representing the book cover
    int duration;       // The length of the book in seconds

    public Book (int id, String title, String author, int published, String coverURL, int duration) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.published = published;
        this.coverURL = coverURL;
        this.duration = duration;
    }
}
