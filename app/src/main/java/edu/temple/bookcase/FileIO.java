package edu.temple.bookcase;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileIO {

    private Context context;

    public FileIO(Context context) {
        this.context = context;
    }

    public Book readBookFromStorage(String fileName) {
        FileInputStream fis = null;
        ObjectInputStream is = null;
        Book book = null;
        try {
            fis = context.openFileInput(fileName);
            is = new ObjectInputStream(fis);
            book = (Book) is.readObject();
            is.close();
            fis.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return book;
    }

    public void writeBookToStorage(Book book, String fileName) {
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(book);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File readAudioFromStorage(int bookId) {
        String fileName = String.format("%s.mp3", bookId);
        File file = new File(context.getFilesDir(), fileName);
        return file.exists() ? file : null;

    }

    public void deleteAudioFromStorage(int bookId) {
        File file = readAudioFromStorage(bookId);
        if (file != null)
            file.delete();
    }


    public ArrayList<Book> readBookListFromStorage(String fileName) {
        ArrayList<Book> bookArrayList = null;
        FileInputStream fis = null;
        ObjectInputStream is = null;
        try {
            fis = context.openFileInput(fileName);
            is = new ObjectInputStream(fis);
            bookArrayList = (ArrayList<Book>) is.readObject();
            is.close();
            fis.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return bookArrayList;
    }

    public void writeBookListToStorage(ArrayList<Book> bookArrayList, String fileName) {
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(bookArrayList);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
