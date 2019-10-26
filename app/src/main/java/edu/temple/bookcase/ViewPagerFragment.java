package edu.temple.bookcase;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerFragment extends Fragment {

    private final static String booksArrayKey = "books";
//    MyFragmentStatePagerAdapter mAdapter;
//    ViewPager mPager;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    public static ViewPagerFragment newInstance(String[] books) {
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putStringArray(booksArrayKey, books);
        viewPagerFragment.setArguments(args);
        return viewPagerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
//        String[] books = getResources().getStringArray(R.array.books);
//        mAdapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), books);
//
//        mPager = view.findViewById(R.id.viewPager);
//        mPager.setAdapter(mAdapter);

        TextView textView = view.findViewById(R.id.titleTextView);
        String title = getArguments().getString("bookTitle");
        textView.setText(title);

        return view;
    }
//
//    public Boolean isPortraitOrientation() {
//        int orientation = this.getResources().getConfiguration().orientation;
//        return orientation == Configuration.ORIENTATION_PORTRAIT;
//    }
//
//    public static class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
//
//        String[] books;
//
//        public MyFragmentStatePagerAdapter(FragmentManager fm, String[] books) {
//            super(fm);
//            this.books = books;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
////            return BookDetailsFragment.newInstance(books[position]);
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return this.books.length;
//        }
//
//    }

}
