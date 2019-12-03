package edu.temple.bookcase;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment implements BookDetailsFragment.DetailsOnPlaySelectedListener {

    private View view;
    private ArrayList<Book> books;
    private ArrayList<Book> completeBooks;
    private int currentBookId;
    private ViewPager mPager;
    private MyFragmentStatePagerAdapter mAdapter;
    private final static String completeBookListKey = "completeBookListKey";
    private final static String bookArrayListKey = "bookArrayListKey";
    private final static String currentBookIdKey = "currentBookIdKey";
    private OnPlaySelectedListener listenerCallback;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    public static ViewPagerFragment newInstance(ArrayList<Book> filteredBookList, ArrayList<Book> completeBookList, int currentBookId) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putSerializable(bookArrayListKey, filteredBookList);
        args.putSerializable(completeBookListKey, completeBookList);
        args.putInt(currentBookIdKey, currentBookId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            books = (ArrayList<Book>) args.getSerializable(bookArrayListKey);
            completeBooks = (ArrayList<Book>) args.getSerializable(completeBookListKey);
            currentBookId = args.getInt(currentBookIdKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentContainer1
        view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        if (books != null) {
            mPager = view.findViewById(R.id.viewPager);
            mAdapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), books, this);
            mPager.setAdapter(mAdapter);
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentBookId = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            if (currentBookId > 0) {
                mPager.setCurrentItem(currentBookId);
            }
        }

        return view;
    }

    public void filterViewPager(ArrayList<Book> filteredBooks) {
        if (mAdapter != null) {
            mAdapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), filteredBooks, this);
            mAdapter.notifyDataSetChanged();
            mPager.setAdapter(mAdapter);
        }

        if (getArguments() != null) {
            getArguments().putSerializable(bookArrayListKey, filteredBooks);
        }
    }

    public ArrayList<Book> getBooks() {
        return this.books;
    }

    public ArrayList<Book> getCompleteBooks() {
        return this.completeBooks;
    }

    public int getCurrentBookId() {
        return this.currentBookId;
    }

    public void setOnPlaySelectedListener(OnPlaySelectedListener callback) {
        this.listenerCallback = callback;
    }

    public interface OnPlaySelectedListener {
        void pagerOnPlaySelected(int bookId);
    }

    @Override
    public void detailsOnPlaySelected(int bookId) {
        listenerCallback.pagerOnPlaySelected(bookId);
    }
}
