package org.sco.movieratings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment())
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
       switch(item.getItemId()) {
           case R.id.menu_sort_by_top_rated:
               return true;
           case R.id.menu_sort_by_popular:
               return true;
           case R.id.menu_sort_by_my_favorites:
               return true;
           default:
               return super.onOptionsItemSelected(item);
       }

//        if (id == R.id.sort_by) {
//            startActivity(new Intent(this, SettingsActivity.class));
//            return true;
//        }

    }

    // For a dynamic title bar
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(getText(R.string.app_name) + " " + title);
    }
}
