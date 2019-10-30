package edu.temple.bookcase;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerFragment extends Fragment {

    private View view;
    private String[] books;
    private ViewPager mPager;
    private MyFragmentStatePagerAdapter mAdapter;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        books = getResources().getStringArray(R.array.books);
        mPager = view.findViewById(R.id.viewPager);
        mAdapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), books);
        mPager.setAdapter(mAdapter);
        return view;
    }

}
