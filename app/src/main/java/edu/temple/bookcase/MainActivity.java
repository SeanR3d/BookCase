package edu.temple.bookcase;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements BookListFragment.OnBookSelectedListener {

    FragmentManager fm;
    ArrayList<Book> bookArrayList;
    boolean onePane;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();

        getBookListData();

//        onePane = findViewById(R.id.port_layout) == null;
//
//        fragment = fm.findFragmentById(R.id.port_layout);
//
//        if (fragment == null) {
//            getBookListData();
//            fm.beginTransaction()
//                    .add(R.id.port_layout, ViewPagerFragment.newInstance(bookArrayList))
//                    .commit();
//        }


    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof BookListFragment) {
            BookListFragment bookListFragment = (BookListFragment) fragment;
            bookListFragment.setOnBookSelectedListener(this);
        }
    }

    @Override
    public void OnBookSelected(String bookTitle) {
        BookDetailsFragment bookDetailsFragment = (BookDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.book_details_fragment);

        Book book = getBook(bookTitle);

        if (bookDetailsFragment != null) {
            bookDetailsFragment.updateDetailsView(book);
        } else {
            BookDetailsFragment newFragment = BookDetailsFragment.newInstance(book);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.book_details_fragment, newFragment)
                    .commit();
        }
    }

    public Book getBook(String bookTitle) {
        for (Book book: bookArrayList) {
           if (book.title.equals(bookTitle))
               return book;
        }
        return null;
    }

    public void getBookListData() {

        Thread t = new Thread() {
            @Override
            public void run() {

                final String listURL = "https://kamorris.com/lab/audlib/booksearch.php";
                final String searchURL = "https://kamorris.com/lab/audlib/booksearch.php?search=";

                try {
                    URL bookListURL = new URL(listURL);

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    bookListURL.openStream()));

                    StringBuilder response = new StringBuilder();
                    String tmpResponse;

                    tmpResponse = reader.readLine();
                    while (tmpResponse != null) {
                        response.append(tmpResponse);
                        tmpResponse = reader.readLine();
                    }
                    JSONArray jsonArray= new JSONArray(response.toString());
                    Message msg = Message.obtain();
                    msg.obj = jsonArray;
                    bookListResponseHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    Handler bookListResponseHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            bookArrayList = new ArrayList<>();
            JSONArray responseArray = (JSONArray) msg.obj;
            Book book;
            try {
                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject obj = responseArray.getJSONObject(i);
                    book = new Book(
                            obj.getInt("book_id"),
                            obj.getString("title"),
                            obj.getString("author"),
                            obj.getInt("published"),
                            obj.getString("cover_url")
                    );
                    bookArrayList.add(book);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            fragment = fm.findFragmentById(R.id.view_pager_fragment);
            if (fragment != null) {
                getBookListData();
                fm.beginTransaction()
                        .replace(R.id.view_pager_fragment, ViewPagerFragment.newInstance(bookArrayList))
                        .commit();
            }
//            else if(fragment instanceof ViewPagerFragment) {
//                fm.beginTransaction()
//                        .replace(R.id.port_layout, ViewPagerFragment.newInstance(bookArrayList))
//                        .commit();
//            } else if (fragment instanceof BookListFragment) {
//
//            }

            return false;
        }
    });
}
