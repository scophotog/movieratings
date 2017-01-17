package org.sco.movieratings.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.sco.movieratings.R;
import org.sco.movieratings.SettingsActivity;
import org.sco.movieratings.Utility;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private String mSort;
    private static final String SORT_MODE = "SM";
    private static final String MOVIES_FRAGMENT_TAG = "MFT";
    private static final String MOVIES_FAVORITE_FRAGMENT_TAG = "MFFT";

    private MainActivityFragment mMainActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            // Check if favorites sort otherwise top_rated sort;
            mSort = savedInstanceState.getString(SORT_MODE, getString(R.string.pref_sort_top_rated));
        } else {
            mSort = getSortType();
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mMainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(MOVIES_FRAGMENT_TAG);

        Fragment fragment;

        if (mMainActivityFragment == null) {
            // Check we're not sorting on favorites
            if (!mSort.equals(getString(R.string.pref_sort_my_favorites)) ) {
                fragment = new MainActivityFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment, MOVIES_FRAGMENT_TAG)
                        .commit();
            } else {
                fragment = new MainActivityFavoriteFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment, MOVIES_FAVORITE_FRAGMENT_TAG)
                        .commit();
            }

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_MODE, mSort);
    }

    @Override
    protected void onPause() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().putString(getString(R.string.pref_sort_key),mSort);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        String sortType = Utility.getPreferredSort(this);
        if (sortType != null && !sortType.equals(mSort)) {

            if (!sortType.equals(R.string.pref_sort_my_favorites)) {
                MainActivityFragment maf = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(MOVIES_FRAGMENT_TAG);
                if (maf != null) {
                    maf.onSortChanged();
                }
            } else {
                MainActivityFavoriteFragment maff = (MainActivityFavoriteFragment) getSupportFragmentManager().findFragmentByTag(MOVIES_FAVORITE_FRAGMENT_TAG);
                if(maff !=null) {
                    maff.updateMovies();
                }
            }

        }
        mSort = sortType;
    }

    private String getSortType() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_top_rated));

    }
}
