package edu.temple.bookcase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    String[] books;

    public MyFragmentStatePagerAdapter(FragmentManager fm, String[] books) {
        super(fm);
        this.books = books;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putString("bookTitle", books[position]);
        viewPagerFragment.setArguments(args);
        return viewPagerFragment;

    }

    @Override
    public int getCount() {
        return this.books.length;
    }

}