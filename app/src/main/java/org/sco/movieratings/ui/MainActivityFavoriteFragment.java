package org.sco.movieratings.ui;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sco.movieratings.R;
import org.sco.movieratings.data.MovieColumns;
import org.sco.movieratings.data.MovieProvider;
import org.sco.movieratings.rest.FetchMovieTask;

/**
 * Created by sargenzi on 1/16/17.
 */

public class MainActivityFavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainActivityFavoriteFragment.class.getSimpleName();

    protected static final int CURSOR_LOADER_ID = 0;
    private Cursor mCursor;

    private MovieFavoriteListAdapter mMovieFavoriteListAdapter;

    public MainActivityFavoriteFragment(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Cursor c = getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                null,
                MovieColumns.IS_FAVORITE + " = 1",
                null,
                null,
                null);
        Log.i(LOG_TAG, "cursor count: " + c.getCount());
        if (c != null || c.getCount() == 0) {
            mCursor = c;
        }
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
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

        mMovieFavoriteListAdapter = new MovieFavoriteListAdapter(getActivity(),mCursor);

        recyclerView.setLayoutManager(
                new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false)
        );

        recyclerView.setAdapter(mMovieFavoriteListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "resume called");
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    public void updateMovies() {
        Cursor c = null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_top_rated));

        if (sortType.equals("my_favorites")) {
            c = getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                    null,MovieColumns.IS_FAVORITE + " = 1",null,null);
            Log.i(LOG_TAG, "cursor count: " + c.getCount());
        }

        setTitle();
        mMovieFavoriteListAdapter = new MovieFavoriteListAdapter(getContext(), c);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
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

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),MovieProvider.Movies.CONTENT_URI,
                null,
                MovieColumns.IS_FAVORITE + " = 1",
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieFavoriteListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mMovieFavoriteListAdapter.swapCursor(null);
    }
}
