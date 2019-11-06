package edu.temple.bookcase;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements BookListFragment.OnBookSelectedListener {

    ArrayList<Book> bookArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getBookListData();
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

        if (bookDetailsFragment != null) {
            bookDetailsFragment.updateDetailsView(bookTitle);
        } else {
            BookDetailsFragment newFragment = BookDetailsFragment.newInstance(bookTitle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.book_details_fragment, newFragment)
                    .commit();
        }
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

            return false;
        }
    });
}
