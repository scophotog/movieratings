package org.sco.movieratings.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.sco.movieratings.R;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private String mSort;
    private static final String SORT_MODE = "SM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            mSort = savedInstanceState.getString(SORT_MODE, getString(R.string.pref_sort_top_rated));
        }
        setTitle();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_MODE, mSort);
    }

    @Override
    protected void onPause() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        prefs.edit().putString(getString(R.string.pref_sort_key),mSort).apply();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_by) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle();
    }

    public void setTitle() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortType = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_top_rated));

        String title;
        switch (sortType) {
            case "top_rated":
                title = getString(R.string.high_rated_settings);
                break;
            case "popular":
                title = getString(R.string.most_popular_settings);
                break;
            case "my_favorites":
                title = getString(R.string.my_favorites_settings);
                break;
            default:
                title = "";
                break;
        }

    }

    public void setTitle(final String title) {
        getSupportActionBar().setTitle(getText(R.string.app_name) + " " + title);
    }
}
