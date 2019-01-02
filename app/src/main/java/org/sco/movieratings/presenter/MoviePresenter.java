package org.sco.movieratings.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.sco.movieratings.R;
import org.sco.movieratings.api.models.Movie;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by sargenzi on 1/20/17.
 */

public class MoviePresenter {

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private TextView mMovieDetails;
    private TextView mMovieReleaseDate;
    private TextView mMovieVoteAverage;
    private TextView mEmptyTextView;

    Context mContext;
    
    public MoviePresenter(@NonNull View view) {
        mContext = view.getContext();
        mMovieTitle = view.findViewById(R.id.movie_title);
        mMoviePoster = view.findViewById(R.id.poster);
        mMovieDetails = view.findViewById(R.id.movie_details);
        mMovieReleaseDate = view.findViewById(R.id.release_date);
        mMovieVoteAverage = view.findViewById(R.id.vote_average);
        mEmptyTextView = view.getRootView().findViewById(R.id.empty_detail_view);
    }

    public void present(@NonNull Movie movie) {
        if(mEmptyTextView != null) {
            mEmptyTextView.setVisibility(GONE);
        }

        String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";

        mMovieTitle.setText(movie.getTitle());
        mMovieTitle.setVisibility(VISIBLE);
        mMovieDetails.setText(movie.getOverview());

        Picasso.with(mContext)
                .load(IMAGE_PATH + movie.getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(mMoviePoster);

        mMovieReleaseDate.setText(mContext.getString(R.string.format_release_date, movie.getReleaseDate().split("-")[0]));
        mMovieVoteAverage.setText(mContext.getString(R.string.format_rating, movie.getVoteAverage()));
    }
    
}
