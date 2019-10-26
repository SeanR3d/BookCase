package edu.temple.bookcase;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class MainActivity extends FragmentActivity {

    MyFragmentStatePagerAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get books array
        String[] books = getResources().getStringArray(R.array.books);
//
//        // Add books array to bundle
//        ViewPagerFragment viewPagerFragment = ViewPagerFragment.newInstance(books);
//        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
//
//        // Add ViewPagerFragment to FragmentManager
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.viewpager_fragment, viewPagerFragment)
//                .addToBackStack(null)
//                .commit();


        this.mPager = (ViewPager) findViewById(R.id.viewPager);
        this.mAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager(),books);
        mPager.setAdapter(mAdapter);

    }
    public static class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        String[] books;

        public MyFragmentStatePagerAdapter(FragmentManager fm, String[] books) {
            super(fm);
            this.books = books;
        }

        @Override
        public Fragment getItem(int position) {
//            return BookDetailsFragment.newInstance(books[position]);
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

}
