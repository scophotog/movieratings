package org.sco.movieratings.activity;

import android.os.Bundle;

import org.sco.movieratings.R;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private String mSort;
    private static final String SORT_MODE = "SM";
    private static boolean picassoInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            mSort = savedInstanceState.getString(SORT_MODE, getString(R.string.pref_sort_top_rated));
        }
        if (!picassoInitialized) {
            Picasso.setSingletonInstance(new Picasso.Builder(getApplicationContext()).build());
            picassoInitialized = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_MODE, mSort);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setTitle(final String title) {
        getSupportActionBar().setTitle(getText(R.string.app_name) + " " + title);
    }
}
