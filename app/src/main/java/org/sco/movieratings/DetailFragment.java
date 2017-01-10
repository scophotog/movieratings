package org.sco.movieratings;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sco.movieratings.data.MovieContract;

import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    static final String DETAIL_URI = "URI";
    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_IS_FAVORITE
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_API_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_POSTER_PATH = 3;
    static final int COL_RELEASE_DATE = 4;
    static final int COL_OVERVIEW = 5;
    static final int COL_RATING = 6;
    static final int COL_POPULARITY = 7;
    static final int COL_IS_FAVORITE = 8;


    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private TextView mMovieDetails;
    private TextView mMovieReleaseDate;
    private TextView mMovieVoteAverage;

    public DetailFragment() {
                          setHasOptionsMenu(true);
                                                  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mMovieTitle = (TextView) rootView.findViewById(R.id.movie_title_detail);
        mMovieDetails = (TextView) rootView.findViewById(R.id.movie_details);
        mMoviePoster = (ImageView) rootView.findViewById(R.id.poster);
        mMovieReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
        mMovieVoteAverage = (TextView) rootView.findViewById(R.id.vote_average);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        return new CursorLoader(
                getActivity(),
                intent.getData(),
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (data != null && data.moveToFirst()) {

            mMovieTitle.setText(data.getString(COL_MOVIE_TITLE));
            mMovieDetails.setText(data.getString(COL_OVERVIEW));

            Picasso.with(getActivity())
                    .load(data.getString(COL_POSTER_PATH))
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.image_not_found)
                    .into(mMoviePoster);

            mMovieReleaseDate.setText(data.getString(COL_RELEASE_DATE));

            mMovieVoteAverage.setText(data.getString(COL_RATING));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
