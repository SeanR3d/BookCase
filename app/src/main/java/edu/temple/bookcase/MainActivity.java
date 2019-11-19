package edu.temple.bookcase;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
    ArrayList<Book> prevFilterBookList;
    boolean onePane;
    Fragment fragmentContainer1;
    Fragment fragmentContainer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();

        onePane = findViewById(R.id.viewPagerContainer) != null;
        fragmentContainer1 = fm.findFragmentById(R.id.viewPagerContainer);
        fragmentContainer2 = fm.findFragmentById(R.id.bookListContainer);

        final Fragment bookDetailsFragment = fm.findFragmentById(R.id.book_details_fragment);
        if (bookDetailsFragment != null) {
            fm.beginTransaction()
                    .remove(bookDetailsFragment)
                    .commit();
        }

        if (onePane) {
            if (fragmentContainer1 == null) {
                if (fragmentContainer2 != null) {
                    bookArrayList = ((BookListFragment) fragmentContainer2).getCompleteBooks();
                    prevFilterBookList = ((BookListFragment) fragmentContainer2).getBooks();
                    ViewPagerFragment viewPagerFragment = ViewPagerFragment.newInstance(prevFilterBookList, bookArrayList);
                    fm.beginTransaction()
                            .remove(fragmentContainer2)
                            .add(R.id.viewPagerContainer, viewPagerFragment)
                            .commit();
                } else {
                    fm.beginTransaction()
                            .add(R.id.viewPagerContainer, new ViewPagerFragment())
                            .commit();
                    getBookListData();
                }
            }
        } else {
            if (fragmentContainer2 == null) {
                if (fragmentContainer1 != null) {
                    bookArrayList = ((ViewPagerFragment) fragmentContainer1).getCompleteBooks();
                    prevFilterBookList = ((ViewPagerFragment) fragmentContainer1).getBooks();
                    BookListFragment bookListFragment = BookListFragment.newInstance(prevFilterBookList, bookArrayList);
                    fm.beginTransaction()
                            .remove(fragmentContainer1)
                            .add(R.id.bookListContainer, bookListFragment)
                            .commit();

                } else {
                    fm.beginTransaction()
                            .add(R.id.bookListContainer, new BookListFragment())
                            .add(R.id.BookDetailsContainer, new BookDetailsFragment())
                            .commit();
                    getBookListData();
                }
            } else {
                bookArrayList = ((BookListFragment) fragmentContainer2).getBooks();
                fm.beginTransaction()
                        .replace(R.id.bookListContainer, BookListFragment.newInstance(bookArrayList, bookArrayList))
                        .commit();
            }
        }

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.searchEditText);
                ArrayList<Book> filterBooksArrayList = filterBooks(editText.getText().toString());
                if (onePane) {
                    ViewPagerFragment viewPagerFragment = (ViewPagerFragment) fm.findFragmentById(R.id.viewPagerContainer);
                    if (viewPagerFragment != null) {
                        viewPagerFragment.filterViewPager(filterBooksArrayList);
                    }
                } else {
                    BookListFragment bookListFragment = (BookListFragment) fm.findFragmentById(R.id.bookListContainer);
                    if (bookListFragment != null) {
                        bookListFragment.filterArrayAdapter(filterBooksArrayList);
                    }
                }

            }
        });
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

        BookDetailsFragment bookDetailsFragment = null;
        Book book = getBook(bookTitle);
        Fragment bookListFragment = fm.findFragmentById(R.id.bookListContainer);

        try {
            FragmentManager child = bookListFragment.getFragmentManager();
            bookDetailsFragment = (BookDetailsFragment) child.findFragmentById(R.id.BookDetailsContainer);
        } catch (Exception e) {
            Log.d("OnBookSelected", "Exception Thrown!");
        }

        if (bookDetailsFragment != null) {
            bookDetailsFragment.updateDetailsView(book);
        } else {
            BookDetailsFragment newFragment = BookDetailsFragment.newInstance(book);
            fm.beginTransaction()
                    .add(R.id.BookDetailsContainer, newFragment)
                    .commit();
        }
    }

    public Book getBook(String bookTitle) {
        for (Book book : bookArrayList) {
            if (book.title.equals(bookTitle))
                return book;
        }
        return null;
    }

    public ArrayList<Book> filterBooks(String search) {
        ArrayList<Book> filteredResults = new ArrayList<>();

        for (Book book : bookArrayList) {
            if (book.title.contains(search))
                filteredResults.add(book);
            else if (book.author.contains(search))
                filteredResults.add(book);
            else if (String.valueOf(book.published).contains(search))
                filteredResults.add(book);
        }

        return filteredResults;
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
                    JSONArray jsonArray = new JSONArray(response.toString());
                    Message msg = Message.obtain();
                    msg.obj = jsonArray;
                    Log.d("HTTP GET", "Successfully retrieved data");
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
                Log.d("Handler", "Created bookArrayList of size:" + bookArrayList.size() + ".");

            } catch (Exception e) {
                e.printStackTrace();
            }

            fragmentContainer1 = fm.findFragmentById(R.id.viewPagerContainer);
            fragmentContainer2 = fm.findFragmentById(R.id.bookListContainer);

            if (fragmentContainer1 != null) {
                ViewPagerFragment viewPagerFragment = ViewPagerFragment.newInstance(bookArrayList, bookArrayList);
                fm.beginTransaction()
                        .remove(fragmentContainer1)
                        .add(R.id.viewPagerContainer, viewPagerFragment)
                        .commit();
            } else if (fragmentContainer2 != null) {
                BookListFragment bookListFragment = BookListFragment.newInstance(bookArrayList, bookArrayList);
                fm.beginTransaction()
                        .remove(fragmentContainer2)
                        .add(R.id.bookListContainer, bookListFragment)
                        .commit();
            }

            return false;
        }
    });
}
