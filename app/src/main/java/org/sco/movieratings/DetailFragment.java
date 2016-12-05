package org.sco.movieratings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by sargenzi on 12/1/16.
 */

public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private String mMovieTitle;
    private String mMoviePoster;
    private String mMovieDetails;
    private String mMovieReleaseDate;

    public DetailFragment() {
                          setHasOptionsMenu(true);
                                                  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();

        if (intent != null) {
            Movie movieObj = intent.getParcelableExtra("movie");

            mMovieTitle = movieObj.title;
            ((TextView) rootView.findViewById(R.id.movie_title_detail))
                    .setText(mMovieTitle);

            mMovieDetails = movieObj.overview;
            ((TextView) rootView.findViewById(R.id.movie_details))
                    .setText(mMovieDetails);

            mMoviePoster = movieObj.poster_path;
            ImageView image = (ImageView) rootView.findViewById(R.id.poster);
            Picasso.with(getActivity())
                    .load(mMoviePoster)
                    .into(image);

            mMovieReleaseDate = movieObj.release_date;
            ((TextView) rootView.findViewById(R.id.release_date))
                    .setText(mMovieReleaseDate);
        }

        return rootView;
    }

}
