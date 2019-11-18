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

/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment {

    private OnBookSelectedListener callback;
    private Context parent;
    private ArrayList<Book> books;
    private ArrayList<String> bookTitles;
    private View view;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private final static String bookArrayListKey = "bookArrayListKey";
    private final static String bookTitlesKey = "bookTitlesKey";


    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parent = context;
    }

    public static BookListFragment newInstance(ArrayList<Book> bookArrayList) {

        ArrayList<String> bookTitles = new ArrayList<>();
        for (Book book : bookArrayList)
            bookTitles.add(book.title);

        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putSerializable(bookArrayListKey, bookArrayList);
        args.putSerializable(bookTitlesKey, bookTitles);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            books = (ArrayList<Book>)getArguments().getSerializable(bookArrayListKey);
            bookTitles = (ArrayList<String>)getArguments().getSerializable(bookTitlesKey);
        }
    }

    public void setOnBookSelectedListener(OnBookSelectedListener callback) {
        this.callback = callback;
    }

    public interface OnBookSelectedListener {
        void OnBookSelected(String bookTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentContainer1
        view = inflater.inflate(R.layout.fragment_book_list, container, false);

        if(books != null) {
            listView = view.findViewById(R.id.bookListView);
            adapter = new ArrayAdapter<>(parent, android.R.layout.simple_list_item_1, bookTitles);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String bookTitle = adapter.getItem(position);
                    callback.OnBookSelected(bookTitle);
                }
            });
        }

        return view;
    }

    public ArrayList<Book> getBooks() {
        return this.books;
    }

}
