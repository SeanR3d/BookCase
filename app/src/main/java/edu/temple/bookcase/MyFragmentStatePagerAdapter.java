package edu.temple.bookcase;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Book> books;
    private BookDetailsFragment.DetailsOnPlaySelectedListener listener;

    public MyFragmentStatePagerAdapter(FragmentManager fm, ArrayList<Book> books,
                                       BookDetailsFragment.DetailsOnPlaySelectedListener detailsOnPlaySelectedListener) {
        super(fm);
        this.books = books;
        this.listener = detailsOnPlaySelectedListener;
    }

    @Override
    public Fragment getItem(int position) {
        BookDetailsFragment bookDetailsFragment = BookDetailsFragment.newInstance(books.get(position), listener);
        return bookDetailsFragment;
    }

    @Override
    public int getCount() {
        return this.books.size();
    }

}