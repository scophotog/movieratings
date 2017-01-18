package org.sco.movieratings.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.sco.movieratings.R;
import org.sco.movieratings.data.models.Movie;
import org.sco.movieratings.data.rest.FetchMovieTask;

/**
 * Created by sargenzi on 1/17/17.
 */

public class MovieListLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String LOG_TAG = MovieListLoader.class.getSimpleName();

    List<Movie> mMovies;
    Context mContext;

    public MovieListLoader(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public List<Movie> loadInBackground() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String sortType = prefs.getString(mContext.getString(R.string.pref_sort_key),
                mContext.getString(R.string.pref_sort_top_rated));

        List<Movie> out = new ArrayList<>();

        if (!sortType.equals("my_favorites")) {
            FetchMovieTask movieTask = new FetchMovieTask(sortType);
            out.addAll(movieTask.getResults());
        } else {
            Cursor c = mContext.getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                    null,
                    MovieColumns.IS_FAVORITE + " = 1",
                    null,
                    null,
                    null);

            if (c != null && c.moveToFirst()) {
                do {
                    out.add(Movie.fromCursor(c));
                } while (c.moveToNext());
            } else {
                Log.e(LOG_TAG, "Cursor: Null");
            }

        }

        return out;
    }

    @Override
    public void deliverResult(List<Movie> movies) {
        if(isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (movies != null) {
                onReleaseResources(movies);
            }
        }

        List<Movie> oldMovies = mMovies;
        mMovies = movies;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(movies);
        }

        if (oldMovies != null) {
            onReleaseResources(oldMovies);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override protected void onStartLoading() {
        if (mMovies != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mMovies);
        }

        if (takeContentChanged() || mMovies == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override public void onCanceled(List<Movie> movies) {
        super.onCanceled(movies);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(movies);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mMovies != null) {
            onReleaseResources(mMovies);
            mMovies = null;
        }

    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(List<Movie> movies) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }
}
