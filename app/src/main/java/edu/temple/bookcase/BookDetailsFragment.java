package edu.temple.bookcase;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class BookDetailsFragment extends Fragment {

    private final static String bookObjKey = "bookObjKey";
    private Book book;
    private View view;
    private ImageView bookCoverImageView;
    private TextView bookTitleTextView;
    private TextView bookAuthorTextView;
    private TextView bookPublishedTextView;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(bookObjKey, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            book = (Book)getArguments().getSerializable(bookObjKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentContainer1
        view = inflater.inflate(R.layout.fragment_book_details, container, false);

        if (book != null) {
            bookCoverImageView = view.findViewById(R.id.bookCoverImageView);
            bookTitleTextView = view.findViewById(R.id.bookTitleTextView);
            bookAuthorTextView = view.findViewById(R.id.bookAuthorTextView);
            bookPublishedTextView = view.findViewById(R.id.bookPublishedTextView);

            Picasso.with(getContext()).load(book.coverURL).into(bookCoverImageView);
            bookTitleTextView.setText(book.title);
            bookAuthorTextView.setText(book.author);
            bookPublishedTextView.setText(String.valueOf(book.published));

        }

        return view;
    }

    public void updateDetailsView(Book book) {
        bookCoverImageView = view.findViewById(R.id.bookCoverImageView);
        bookTitleTextView = view.findViewById(R.id.bookTitleTextView);
        bookAuthorTextView = view.findViewById(R.id.bookAuthorTextView);
        bookPublishedTextView = view.findViewById(R.id.bookPublishedTextView);

        Picasso.with(getContext()).load(book.coverURL).into(bookCoverImageView);
        bookTitleTextView.setText(book.title);
        bookAuthorTextView.setText(book.author);
        bookPublishedTextView.setText(String.valueOf(book.published));
    }

}
