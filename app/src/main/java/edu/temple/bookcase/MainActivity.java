package edu.temple.bookcase;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import edu.temple.audiobookplayer.AudiobookService;
import edu.temple.audiobookplayer.AudiobookService.BookProgress;

public class MainActivity extends FragmentActivity implements BookListFragment.OnBookSelectedListener,
        ViewPagerFragment.OnPlaySelectedListener, BookDetailsFragment.DetailsOnPlaySelectedListener {

    FragmentManager fm;
    ArrayList<Book> bookArrayList;
    ArrayList<Book> prevFilterBookList;
    boolean onePane;
    Fragment fragmentContainer1;
    Fragment fragmentContainer2;
    Intent serviceIntent;
    AudiobookService.MediaControlBinder binder;
    AudiobookService.BookProgress bookProgress;
    boolean connected;
    TextView header;
    SeekBar seekBar;
    Button downloadButton;
    int currentBookProgress;
    int currentBookId;
    int pausedBookId;
    final static String bookProgressKey = "bookProgressKey";
    final static String currentBookIdKey = "currentBookIdKey";
    final static String pauseText = "Paused";
    final static String stopText = "Stopped";
    final static String nowPlayingText = "Now Playing: ";
    final static String deleteText = "Delete";
    final static String downloadText = "Download";
    String currentBookFileName = "currentBook";
    Book currentBook;

    ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (AudiobookService.MediaControlBinder) service;
            connected = true;
            binder.setProgressHandler(bookProgressHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        serviceIntent = new Intent(this, AudiobookService.class);
        bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (connected) {
            unbindService(myConnection);
            connected = false;
        }
        currentBook.setProgress(0);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
//        savedInstanceState.putInt(currentBookIdKey, currentBookId);
//        savedInstanceState.putInt(bookProgressKey, currentBookProgress);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        currentBookId = savedInstanceState.getInt(currentBookIdKey);
//        currentBookProgress = savedInstanceState.getInt(bookProgressKey);
//        Log.d("Restore", "Restored: " + currentBookId + " " + currentBookProgress + " ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentBook = getBookFromStorage(currentBookFileName);

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
                    currentBookId = ((BookListFragment) fragmentContainer2).getCurrentBookId();
                    if (currentBook != null)
                        currentBookId = currentBook.id;
                    ViewPagerFragment viewPagerFragment = ViewPagerFragment.newInstance(prevFilterBookList, bookArrayList, currentBookId);
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
                    currentBookId = ((ViewPagerFragment) fragmentContainer1).getCurrentBookId();
                    BookListFragment bookListFragment = BookListFragment.newInstance(prevFilterBookList, bookArrayList, currentBookId);
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
                currentBookId = ((BookListFragment) fragmentContainer2).getCurrentBookId();
                fm.beginTransaction()
                        .replace(R.id.bookListContainer, BookListFragment.newInstance(bookArrayList, bookArrayList, currentBookId))
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

        header = findViewById(R.id.headerTextView);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                binder.seekTo(progress);
            }
        });

        findViewById(R.id.pauseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseBook();
            }
        });

        findViewById(R.id.stopButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBook();
            }
        });

        downloadButton = findViewById(R.id.downloadButton);
        if (true) {  // TODO replace
            downloadButton.setText(deleteText);
        } else {
            downloadButton.setText(downloadText);
        }

        findViewById(R.id.downloadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadBookData(currentBook.id);
            }
        });

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof BookListFragment) {
            BookListFragment bookListFragment = (BookListFragment) fragment;
            bookListFragment.setOnBookSelectedListener(this);

        } else if (fragment instanceof ViewPagerFragment) {
            ViewPagerFragment viewPagerFragment = (ViewPagerFragment) fragment;
            viewPagerFragment.setOnPlaySelectedListener(this);
        }
    }

    @Override
    public void onBookSelected(String bookTitle) {

        BookDetailsFragment bookDetailsFragment = null;
        Book book = getBookByTitle(bookTitle);
        BookListFragment bookListFragment = (BookListFragment) fm.findFragmentById(R.id.bookListContainer);

        try {
            FragmentManager child = bookListFragment.getFragmentManager();
            bookDetailsFragment = (BookDetailsFragment) child.findFragmentById(R.id.BookDetailsContainer);
        } catch (Exception e) {
            Log.d("onBookSelected", "Exception Thrown!");
        }

        if (bookDetailsFragment != null) {
            bookDetailsFragment.updateDetailsView(book, this);
        } else {
            BookDetailsFragment newFragment = BookDetailsFragment.newInstance(book, this);
            fm.beginTransaction()
                    .add(R.id.BookDetailsContainer, newFragment)
                    .commit();
        }
    }

    @Override
    public void pagerOnPlaySelected(int bookId) {
        playBook(bookId);
        displayAudioButtons();
    }

    @Override
    public void detailsOnPlaySelected(int bookId) {
        playBook(bookId);
        displayAudioButtons();
    }

    public void playBook(int bookId) {
        if (connected) {
            currentBook = getBookById(bookId);
            startService(serviceIntent);
            binder.play(bookId);
            currentBookId = currentBook.id;
            seekBar.setMax(currentBook.duration);
            seekBar.setProgress(0);
            header.setText(String.format("%s %s", nowPlayingText, currentBook.title));
            currentBook.saveBookToStorage(this, currentBookFileName);
        }
    }

    public void pauseBook() {
        if (connected) {
            if (binder.isPlaying()) {
                pausedBookId = bookProgress.getBookId();
                binder.pause();
                header.setText(pauseText);
                currentBook.setProgress(bookProgress.getProgress());
                currentBook.saveBookToStorage(this, currentBookFileName);
            } else {
                if (pausedBookId > 0) {
                    binder.play(pausedBookId, seekBar.getProgress());
                    header.setText(String.format("%s %s", nowPlayingText, getBookById(currentBookId).title));
                    pausedBookId = 0;
                }
            }
        }
    }

    public void stopBook() {
        if (connected) {
            stopService(serviceIntent);
            binder.stop();
            header.setText(stopText);
            currentBookId = -1;
        }
    }

    public Book getBookByTitle(String bookTitle) {
        for (Book book : bookArrayList) {
            if (book.title.equals(bookTitle))
                return book;
        }
        return null;
    }

    public Book getBookById(int bookId) {
        for (Book book : bookArrayList) {
            if (book.id == bookId)
                return book;
        }
        return null;
    }

    public void displayAudioButtons() {
        seekBar.setMax(getBookById(currentBookId).duration);
        seekBar.setVisibility(View.VISIBLE);
        findViewById(R.id.pauseButton).setVisibility(View.VISIBLE);
        findViewById(R.id.stopButton).setVisibility(View.VISIBLE);
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

    public void saveBookToStorage(Book book, String fileName) {
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Book getBookFromStorage(String fileName) {
        FileInputStream fis = null;
        ObjectInputStream is = null;
        Book book = null;
        try {
            fis = getApplicationContext().openFileInput(fileName);
            is = new ObjectInputStream(fis);
            book = (Book) is.readObject();
            is.close();
            fis.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return book;
    }

    public void downloadBookData(final int bookId) {
        Thread t = new Thread() {
            @Override
            public void run() {

                final String downloadURL = "https://kamorris.com/lab/audlib/download.php?id=";
                final String bookDownloadURLString = String.format("%s%s", downloadURL, bookId);

                try {
                    URL bookURL = new URL(bookDownloadURLString);
                    File file = new File(getFilesDir(), String.format("%s.mp3", bookId));

                    InputStream is = new BufferedInputStream(bookURL.openStream());
                    file.createNewFile();
                    OutputStream os = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }

                    is.close();
                    os.flush();
                    os.close();
                    Log.d("DOWNLOAD", "Successfully downloaded data");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
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
                            obj.getString("cover_url"),
                            obj.getInt("duration")
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
                ViewPagerFragment viewPagerFragment = ViewPagerFragment.newInstance(bookArrayList, bookArrayList, currentBookId);
                fm.beginTransaction()
                        .remove(fragmentContainer1)
                        .add(R.id.viewPagerContainer, viewPagerFragment)
                        .commit();
            } else if (fragmentContainer2 != null) {
                BookListFragment bookListFragment = BookListFragment.newInstance(bookArrayList, bookArrayList, currentBookId);
                fm.beginTransaction()
                        .remove(fragmentContainer2)
                        .add(R.id.bookListContainer, bookListFragment)
                        .commit();
            }

            return false;
        }
    });

    Handler bookProgressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            bookProgress = (BookProgress) msg.obj;
            if (bookProgress != null) {
                currentBookId = bookProgress.getBookId();
                currentBookProgress = bookProgress.getProgress();
                if (binder.isPlaying()) {
                    header.setText(String.format("Now playing: %s", getBookById(currentBookId).title));
                    displayAudioButtons();
                }
                seekBar.setProgress(currentBookProgress);
            }
            Log.d("handler", "book: " + currentBookId + " -- progress: " + currentBookProgress + ". ");

            return false;
        }

    });
}
