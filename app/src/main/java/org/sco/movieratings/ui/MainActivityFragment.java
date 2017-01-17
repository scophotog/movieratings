package org.sco.movieratings.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sco.movieratings.data.MovieColumns;
import org.sco.movieratings.data.MovieProvider;
import org.sco.movieratings.data.models.Movie;
import org.sco.movieratings.rest.FetchMovieTask;
import org.sco.movieratings.R;

/**
 * The activity for displaying all movie posters.
 */

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private static final String SAVED_MOVIES = "movies";
    private MovieListAdapter mMovieListAdapter;

    private List<Movie> mMovies;

    private Callbacks mCallbacks = sDummyCallbacks;

    private static final int MOVIES_LOADER = 0;


    public interface Callbacks {
        public void onItemSelected(Movie movie);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {

        @Override
        public void onItemSelected(Movie movie) {

        }
    };

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.movie_list);
        recyclerView.setHasFixedSize(true);

        mMovies = new ArrayList<>();
        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(SAVED_MOVIES);
        }

        mMovieListAdapter = new MovieListAdapter(getActivity(), mMovies, mCallbacks);

        recyclerView.setLayoutManager(
                new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false)
        );

        recyclerView.setAdapter(mMovieListAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_MOVIES, new ArrayList<>(mMovieListAdapter.getItems()));
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void setTitle() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_top_rated));

        String title;
        switch (sortType) {
            case "top_rated":
                title = getString(R.string.high_rated_settings);
                break;
            case "most_popular":
                title = getString(R.string.most_popular_settings);
                break;
            case "my_favorites":
                title = getString(R.string.my_favorites_settings);
                break;
            default:
                title = "";
                break;
        }
        ((MainActivity) getActivity()).getSupportActionBar()
                .setTitle(getText(R.string.app_name) + " " + title);
    }

    public void onSortChanged() {
        updateMovies();
    }

    private void updateMovies() {
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        setTitle();
        return new MovieListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mMovies.clear();
        mMovies.addAll(data);
        mMovieListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mMovies.clear();
        mMovieListAdapter.notifyDataSetChanged();
    }

    public static class MovieListLoader extends AsyncTaskLoader<List<Movie>> {

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

}
