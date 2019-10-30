package edu.temple.bookcase;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BookDetailsFragment extends Fragment {

    private final static String bookTitleKey = "bookTitleKey";
    private View view;
    private TextView textView;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(String bookTitle) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putString(bookTitleKey, bookTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_details, container, false);

        if (getArguments() != null) {
            textView = view.findViewById(R.id.bookTitleTextView);
            String title = getArguments().getString(bookTitleKey);
            textView.setText(title);
        }

        return view;
    }

    public void updateDetailsView(String bookTitle) {
        TextView bookTitleTextView = view.findViewById(R.id.bookTitleTextView);
        bookTitleTextView.setText(bookTitle);
    }

}
