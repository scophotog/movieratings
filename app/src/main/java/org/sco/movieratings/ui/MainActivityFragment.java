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

import org.sco.movieratings.MovieListLoader;
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

}
