package org.sco.movieratings;

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


/**
 * The activity for displaying all movie posters.
 */

public class MainActivityFragment extends Fragment {

    private MovieAdapter mMovieAdapter;

    private ArrayList<Movie> movieList;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            movieList = new ArrayList<Movie>();
        } else {
            movieList = savedInstanceState.getParcelableArrayList("movies");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(movieList);
        rv.setAdapter(mMovieAdapter);

        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2,
                GridLayoutManager.VERTICAL, false);

        rv.setLayoutManager(glm);
    }

    private void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask(mMovieAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_top_rated));
        movieTask.execute(sortType);

        if (sortType.equals("top_rated")) {
            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.high_rated_settings));
        } else if (sortType.equals("most_popular")) {
            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.most_popular_settings));
        } else {
            ((MainActivity) getActivity()).setActionBarTitle("");
        }

        mMovieAdapter = movieTask.getResults();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

}
