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

import org.sco.movieratings.MovieActivity;
import org.sco.movieratings.MovieFragment;
import org.sco.movieratings.R;
import org.sco.movieratings.SettingsActivity;
import org.sco.movieratings.Utility;
import org.sco.movieratings.data.models.Movie;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity
        implements MainActivityFragment.Callbacks {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private String mSort;
    private boolean mTwoPane;
    private static final String SORT_MODE = "SM";
    private static final String MOVIE_DETAIL_FRAGMENT_TAG = "MDFT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieFragment(), MOVIE_DETAIL_FRAGMENT_TAG)
                        .commit();
            }

        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        if (savedInstanceState != null) {
            // Check if favorites sort otherwise top_rated sort;
            mSort = savedInstanceState.getString(SORT_MODE, getString(R.string.pref_sort_top_rated));
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
        String sortType = Utility.getPreferredSort(this);
        if (sortType != null && !sortType.equals(mSort)) {
            MainActivityFragment maf = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.movie_list_container);
            if (maf != null) {
                maf.onSortChanged();
            }
        }
        mSort = sortType;
        setTitle();
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            findViewById(R.id.pick_a_movie_text).setVisibility(GONE);
            Bundle args = new Bundle();
            args.putParcelable(MovieFragment.MOVIE, movie);

            MovieFragment fragment = new MovieFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MOVIE_DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieActivity.class)
                    .putExtra(MovieFragment.MOVIE, movie);
            startActivity(intent);
        }
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
            case "most_popular":
                title = getString(R.string.most_popular_settings);
                break;
            case "my_favorites":
                title = getString(R.string.my_favorites_settings);
                break;
            default:
                title = "";
                break;
        }
        getSupportActionBar().setTitle(getText(R.string.app_name) + " " + title);
    }
}
