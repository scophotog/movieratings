package org.sco.movieratings.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.MovementMethod;

import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.fragment.MovieFragment;
import org.sco.movieratings.R;
import org.sco.movieratings.fragment.MovieListRouter;

public class MovieActivity extends AppCompatActivity {

    private final String LOG_TAG = MovieActivity.class.getSimpleName();

    public static MovieActivity newInstance(Movie movie, Context context) {
        MovieActivity activity = new MovieActivity();

        Intent intent = new Intent(context, MovieActivity.class)
                    .putExtra(MovieFragment.MOVIE, (Parcelable) movie);
        activity.setIntent(intent);

        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            MovieListRouter router = new MovieListRouter(getSupportFragmentManager());
            router.startFragment((Movie) getIntent().getParcelableExtra(MovieFragment.MOVIE));
        }
    }

}
