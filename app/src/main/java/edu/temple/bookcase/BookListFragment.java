package edu.temple.bookcase;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BookListFragment extends Fragment {

    private Context parent;
    private ArrayList<Book> books;
    private ArrayList<Book> completeBooks;
    private ArrayList<String> bookTitles;
    private View view;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private final static String completeBookListKey = "completeBookListKey";
    private final static String bookArrayListKey = "bookArrayListKey";
    private final static String bookTitlesKey = "bookTitlesKey";
    private OnBookSelectedListener selectedCallback;

    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parent = context;
    }

    public static BookListFragment newInstance(ArrayList<Book> filteredBookList, ArrayList<Book> completeBookList) {

        ArrayList<String> bookTitles = getBookTitlesList(filteredBookList);

        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putSerializable(bookArrayListKey, filteredBookList);
        args.putSerializable(completeBookListKey, completeBookList);
        args.putSerializable(bookTitlesKey, bookTitles);
        fragment.setArguments(args);

        return fragment;
    }

    private static ArrayList<String> getBookTitlesList(ArrayList<Book> bookArrayList) {
        ArrayList<String> bookTitles = new ArrayList<>();
        for (Book book : bookArrayList)
            bookTitles.add(book.title);
        return bookTitles;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            books = (ArrayList<Book>) args.getSerializable(bookArrayListKey);
            completeBooks = (ArrayList<Book>) args.getSerializable(completeBookListKey);
            bookTitles = (ArrayList<String>) args.getSerializable(bookTitlesKey);
        }
    }

    public void setOnBookSelectedListener(OnBookSelectedListener callback) {
        this.selectedCallback = callback;
    }

    public interface OnBookSelectedListener {
        void OnBookSelected(String bookTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentContainer1
        view = inflater.inflate(R.layout.fragment_book_list, container, false);

        if (books != null) {
            listView = view.findViewById(R.id.bookListView);
            adapter = new ArrayAdapter<>(parent, android.R.layout.simple_list_item_1, bookTitles);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String bookTitle = adapter.getItem(position);
                    selectedCallback.OnBookSelected(bookTitle);
                }
            });
        }

        return view;
    }

    public void filterArrayAdapter(ArrayList<Book> filteredBooks) {
        ArrayList<String> filteredBookTitles = getBookTitlesList(filteredBooks);
        if (adapter != null) {
            adapter = new ArrayAdapter<>(parent, android.R.layout.simple_list_item_1, filteredBookTitles);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }

        if (getArguments() != null) {
            getArguments().putSerializable(bookArrayListKey, filteredBooks);
        }
    }

    public ArrayList<Book> getBooks() {
        return this.books;
    }

    public ArrayList<Book> getCompleteBooks() {
        return this.completeBooks;
    }

}
