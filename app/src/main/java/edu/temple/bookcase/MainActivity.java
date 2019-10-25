package edu.temple.bookcase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get books array
        String[] books = getResources().getStringArray(R.array.books);

        // Add books array to bundle
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putCharSequenceArray("books", books);
        viewPagerFragment.setArguments(args);

        // Add ViewPagerFragment to FragmentManager
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.viewpager_fragment, viewPagerFragment)
                .addToBackStack(null)
                .commit();

    }

}
