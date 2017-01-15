package org.sco.movieratings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private String mSort;
    private static final String MOVIES_FRAGMENT_TAG = "MFT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSort = Utility.getPreferredSort(this);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment(), MOVIES_FRAGMENT_TAG)
                    .commit();
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            MainActivityFragment maf = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(MOVIES_FRAGMENT_TAG);
            if ( maf != null ) {
                maf.onSortChanged();
            }
        }
        mSort = sortType;
    }

    // For a dynamic title bar
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(getText(R.string.app_name) + " " + title);
    }

    @Override
    public void onItemSelected(Uri movieUri) {
        Intent intent = new Intent(this, MovieActivity.class).setData(movieUri);
        startActivity(intent);
    }
}
