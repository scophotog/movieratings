package org.sco.movieratings.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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

public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private static final String SAVED_MOVIES = "movies";
    private MovieListAdapter mMovieListAdapter;

    private List<Movie> mMovies;

    private Callbacks mCallbacks = sDummyCallbacks;

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


    public void onSortChanged() {
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

    private void updateMovies() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_top_rated));

        if (!sortType.equals("my_favorites")) {
            FetchMovieTask movieTask = new FetchMovieTask(mMovieListAdapter);
            movieTask.execute(sortType);
        } else {
            mMovies.clear();

            Cursor c = getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                    null,
                    MovieColumns.IS_FAVORITE + " = 1",
                    null,
                    null,
                    null);

            if (c != null && c.moveToFirst()) {
                do {
                    mMovies.add(Movie.fromCursor(c));
                } while (c.moveToNext());
            } else {
                Log.e(LOG_TAG, "Cursor: Null");
            }
            mMovieListAdapter.notifyDataSetChanged();
        }
        setTitle();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

}
