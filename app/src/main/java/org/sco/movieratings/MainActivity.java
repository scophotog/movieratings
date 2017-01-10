package org.sco.movieratings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String MOVIEFRAGMENT_TAG = "MTAG";

    private String mSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSort = Utility.getPreferredSort(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment(), MOVIEFRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();

        if (id == R.id.sort_by) {
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
            MainActivityFragment maf = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(MOVIEFRAGMENT_TAG);
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
}
