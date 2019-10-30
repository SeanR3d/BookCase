package edu.temple.bookcase;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends FragmentActivity implements BookListFragment.OnBookSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get books array
        String[] books = getResources().getStringArray(R.array.books);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.book_list_fragment, new BookListFragment());
        ft.add(R.id.book_details_fragment, new BookDetailsFragment());
        ft.commit();


        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            ViewPager mPager = findViewById(R.id.viewPager);
            MyFragmentStatePagerAdapter mAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager(), books);
            mPager.setAdapter(mAdapter);
        }
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
        }else {
            BookDetailsFragment newFragment = BookDetailsFragment.newInstance(bookTitle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.book_details_fragment, newFragment)
                    .commit();
        }
    }
}
