package org.sco.movieratings;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sco.movieratings.data.MovieContract;


/**
 * The activity for displaying all movie posters.
 */

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private MovieListAdapter mMovieListAdapter;

    private static final int MOVIE_LIST_LOADER = 0;

    private static final String SELECTED_KEY = "selected_position";

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_IS_FAVORITE
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_API_ID = 1;
    static final int COL_POSTER_PATH = 2;
    static final int COL_IS_FAVORITE = 3;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * MovieFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri movieId);
    }

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LIST_LOADER, null, this);
        setTitle();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMovieListAdapter = new MovieListAdapter(getActivity(), null);

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_movie_list, container, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mMovieListAdapter);

        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2,
                GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(glm);

        return recyclerView;
    }

    public void onSortChanged() {
        updateMovies();
        getLoaderManager().restartLoader(MOVIE_LIST_LOADER, null, this);
    }

    private void setTitle() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_top_rated));

        switch (sortType) {
            case "top_rated":
                ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.high_rated_settings));
                break;
            case "most_popular":
                ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.most_popular_settings));
                break;
            case "my_favorites":
                ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.my_favorites_settings));
                break;
            default:
                ((MainActivity) getActivity()).setActionBarTitle("");
                break;
        }
    }

    private void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask(getActivity());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_top_rated));

        if (!sortType.equals("my_favorites")) {
            movieTask.execute(sortType);
        }

        setTitle();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri movieUri = MovieContract.MovieEntry.CONTENT_URI;

        String sortType = Utility.getPreferredSort(getContext());

        String selection = null;
        String sortOrder = null;

        switch (sortType) {
            case "most_popular":
                sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
                break;
            case "top_rated":
                sortOrder = MovieContract.MovieEntry.COLUMN_RATING + " DESC";
                break;
            case "my_favorites":
                selection = MovieContract.MovieEntry.COLUMN_IS_FAVORITE + " = \"Y\"";
                break;
            default:
                sortOrder = MovieContract.MovieEntry.COLUMN_RATING + " DESC";
        }

        return new CursorLoader(getActivity(),
                movieUri,
                MOVIE_COLUMNS,
                selection,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mMovieListAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieListAdapter.swapCursor(null);
    }
}
