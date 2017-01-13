package org.sco.movieratings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private String mMovieTitle;
    private String mMoviePoster;
    private String mMovieDetails;
    private String mMovieReleaseDate;
    private String mMovieVoteAverage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        if (intent != null) {
            Movie movieObj = intent.getParcelableExtra("movie");

            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mMovieTitle = movieObj.title;

            ((TextView) findViewById(R.id.movie_title))
                    .setText(mMovieTitle);

            mMovieDetails = movieObj.overview;
            ((TextView) findViewById(R.id.movie_details))
                    .setText(mMovieDetails);

            mMoviePoster = movieObj.poster_path;
            ImageView image = (ImageView) findViewById(R.id.poster);
            Picasso.with(this)
                    .load(mMoviePoster)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.image_not_found)
                    .into(image);

            mMovieReleaseDate = movieObj.release_date.split("-")[0];
            ((TextView) findViewById(R.id.release_date))
                    .setText(mMovieReleaseDate);

            mMovieVoteAverage = movieObj.vote_average;
            ((TextView) findViewById(R.id.vote_average))
                    .setText(mMovieVoteAverage + "/10");

            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Added to favorite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.ic_is_favorite);
                }
            });
        }
        
    }

}
