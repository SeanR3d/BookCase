package edu.temple.bookcase;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Book> books;

    public MyFragmentStatePagerAdapter(FragmentManager fm, ArrayList<Book> books) {
        super(fm);
        this.books = books;
    }

    @Override
    public Fragment getItem(int position) {
        BookDetailsFragment bookDetailsFragment = BookDetailsFragment.newInstance(books.get(position));
        return bookDetailsFragment;
    }

    @Override
    public int getCount() {
        return this.books.size();
    }

}