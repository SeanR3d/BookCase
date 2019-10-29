package edu.temple.bookcase;


import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends FragmentActivity {

    private MyFragmentStatePagerAdapter mAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get books array
        String[] books = getResources().getStringArray(R.array.books);

        this.mPager = findViewById(R.id.viewPager);
        this.mAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager(), books);
        mPager.setAdapter(mAdapter);

//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.view_pager_fragment, new ViewPagerFragment())
//                .commit();

    }

}
