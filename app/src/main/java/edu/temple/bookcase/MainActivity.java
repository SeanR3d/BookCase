package edu.temple.bookcase;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class MainActivity extends FragmentActivity implements BookListFragment.OnBookSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
