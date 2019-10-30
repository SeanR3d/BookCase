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


/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment {

    private OnBookSelectedListener callback;
    Context parent;
    String[] books;
    View view;
    ListView listView;
    ArrayAdapter<String> adapter;


    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parent = context;
    }

    public void setOnBookSelectedListener(OnBookSelectedListener callback) {
        this.callback = callback;
    }

    public interface OnBookSelectedListener {
        public void OnBookSelected(String bookTitle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        if (savedInstanceState == null) {

        }
        listView = view.findViewById(R.id.bookListView);
        books = getResources().getStringArray(R.array.books);
        adapter = new ArrayAdapter<>(parent, android.R.layout.simple_list_item_1, books);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bookTitle = adapter.getItem(position);
                callback.OnBookSelected(bookTitle);
            }
        });



        return view;
    }

}
