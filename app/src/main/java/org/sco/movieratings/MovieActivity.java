package org.sco.movieratings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieActivity extends AppCompatActivity {

    private final String LOG_TAG = MovieActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieFragment.MOVIE_URI, getIntent().getData());

            MovieFragment fragment = new MovieFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

}