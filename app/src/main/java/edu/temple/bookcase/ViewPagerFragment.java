package edu.temple.bookcase;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {

    private View view;
    private ArrayList<Book> books;
    private ViewPager mPager;
    private MyFragmentStatePagerAdapter mAdapter;
    private final static String bookArrayListKey = "bookArrayListKey";

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    public static ViewPagerFragment newInstance(ArrayList<Book> bookArrayList) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putSerializable(bookArrayListKey, bookArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    public ArrayList<Book> getBooks() {
        return this.books;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        if(savedInstanceState != null){
            books = savedInstanceState.getParcelable(bookArrayListKey);
            mPager = view.findViewById(R.id.viewPager);
            mAdapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), books);
            mPager.setAdapter(mAdapter);
        }

        return view;
    }

}
