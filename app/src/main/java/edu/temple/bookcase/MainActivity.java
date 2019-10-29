package edu.temple.bookcase;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get books array
        String[] books = getResources().getStringArray(R.array.books);

        int orientation = this.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            ViewPager mPager = findViewById(R.id.viewPager);
            MyFragmentStatePagerAdapter mAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager(), books);
            mPager.setAdapter(mAdapter);
        }




    }

}
