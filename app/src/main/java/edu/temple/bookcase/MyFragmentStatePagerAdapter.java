package edu.temple.bookcase;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private String[] books;

    public MyFragmentStatePagerAdapter(FragmentManager fm, String[] books) {
        super(fm);
        this.books = books;
    }

    @Override
    public Fragment getItem(int position) {
        BookDetailsFragment bookDetailsFragment = BookDetailsFragment.newInstance(books[position]);
        return bookDetailsFragment;
    }

    @Override
    public int getCount() {
        return this.books.length;
    }

}