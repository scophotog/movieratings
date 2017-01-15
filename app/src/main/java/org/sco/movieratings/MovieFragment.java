package org.sco.movieratings;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sco.movieratings.data.MovieContract;

import com.squareup.picasso.Picasso;

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MovieFragment.class.getSimpleName();

    static final String MOVIE_URI = "URI";
    private Uri mUri;

    private static final int MOVIE_LOADER = 0;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(MovieFragment.MOVIE_URI);
        }

        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MovieActivity) {
            MovieActivity activity = ((MovieActivity) getActivity());

            final Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        favoriteButtonLoader();

        super.onActivityCreated(savedInstanceState);
    }

    private void movieLoader(Cursor data) {
        
        View view = getView();

        TextView mMovieTitle = (TextView) view.findViewById(R.id.movie_title);
        ImageView mMoviePoster = (ImageView) view.findViewById(R.id.poster);
        TextView mMovieDetails = (TextView) view.findViewById(R.id.movie_details);
        TextView mMovieReleaseDate = (TextView) view.findViewById(R.id.release_date);
        TextView mMovieVoteAverage = (TextView) view.findViewById(R.id.vote_average);

        mMovieTitle.setText(data.getString(COL_MOVIE_TITLE));
        mMovieDetails.setText(data.getString(COL_OVERVIEW));

        Picasso.with(getActivity())
                .load(data.getString(COL_POSTER_PATH))
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(mMoviePoster);

        mMovieReleaseDate.setText(data.getString(COL_RELEASE_DATE).split("-")[0]);

        mMovieVoteAverage.setText(data.getString(COL_RATING) + "/10");

    }

    private void favoriteButtonLoader() {
        final FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added to favorite", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fab.setImageResource(R.drawable.ic_is_favorite);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        if (null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (data != null && data.moveToFirst()) {
            movieLoader(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
