package org.sco.movieratings.ui;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private ArrayList<Movie> mMovieList;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey(SAVED_MOVIES)) {
            mMovieList = new ArrayList<Movie>();
        } else {
            mMovieList = savedInstanceState.getParcelableArrayList(SAVED_MOVIES);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVED_MOVIES, mMovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle();
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

        mMovieListAdapter = new MovieListAdapter(getActivity(), null);

        recyclerView.setLayoutManager(
                new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false)
        );

        recyclerView.setAdapter(mMovieListAdapter);
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
        FetchMovieTask movieTask = new FetchMovieTask(mMovieListAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_top_rated));

        if (!sortType.equals("my_favorites")) {
            movieTask.execute(sortType);
        } else {
            // Do query here?
            //        Cursor c = getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
//                null,null,null,null);
//        Log.i(LOG_TAG, "cursor count: " + c.getCount());
        }

        setTitle();
        mMovieListAdapter = movieTask.getResults();
    }

    public void onStart() {
        super.onStart();
        updateMovies();
    }

}
