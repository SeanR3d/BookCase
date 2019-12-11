package edu.temple.bookcase;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Book implements Serializable {

    int id;                 // A system provided ID for the book
    String title;           // The title of the book
    String author;          // The author of the book
    int published;          // The year the book was published
    String coverURL;        // A URL to the image representing the book cover
    int duration;           // The length of the book in seconds
    int progress;   // Progress of audio book since last played

    public Book (int id, String title, String author, int published, String coverURL, int duration) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.published = published;
        this.coverURL = coverURL;
        this.duration = duration;
        this.progress = 0;
    }

    public void setProgress(int progress) {
        if (progress > 10) {
            this.progress = (progress - 10);
        }
    }

    public void saveBookToStorage(Context context, String fileName) {
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
