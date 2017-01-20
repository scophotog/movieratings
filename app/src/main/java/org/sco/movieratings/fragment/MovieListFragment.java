package org.sco.movieratings.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.sco.movieratings.adapter.MovieListAdapter;
import org.sco.movieratings.R;
import org.sco.movieratings.api.models.Movie;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * The activity for displaying all movie posters.
 */

public class MovieListFragment extends Fragment {

    private static final String LOG_TAG = MovieListFragment.class.getSimpleName();

    private static final String SAVED_MOVIES = "movies";
    private MovieListAdapter mMovieListAdapter;
    private MoviesInteractor mMoviesInteractor;
    private CompositeSubscription mCompositeSubscription;

    private List<Movie> mMovies;

    private Callbacks mCallbacks = sDummyCallbacks;

    private static final int MOVIES_LOADER = 0;

    private RecyclerView mRecycler;
    private TextView mEmptyView;

    public interface Callbacks {
        void onItemSelected(Movie movie);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {

        @Override
        public void onItemSelected(Movie movie) {

        }
    };

    public MovieListFragment() {
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
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoviesInteractor = new MoviesInteractor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mEmptyView = (TextView) view.findViewById(R.id.empty_view);
        mRecycler = (RecyclerView) view.findViewById(R.id.movie_list);
        mRecycler.setHasFixedSize(true);


        mMovies = new ArrayList<>();
        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(SAVED_MOVIES);
        }

        mMovieListAdapter = new MovieListAdapter(getActivity(), mMovies, mCallbacks);

        mRecycler.setLayoutManager(
                new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false)
        );

        mRecycler.setAdapter(mMovieListAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_MOVIES, new ArrayList<>(mMovieListAdapter.getItems()));
    }

    private void updateMovies(List<Movie> movies) {
        mMovies.clear();
        mMovies.addAll(movies);
        if (movies.isEmpty()) {
            mRecycler.setVisibility(GONE);
            mEmptyView.setVisibility(VISIBLE);
        } else {
            mRecycler.setVisibility(VISIBLE);
            mEmptyView.setVisibility(GONE);
        }
        mMovieListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCompositeSubscription = new CompositeSubscription();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortType = prefs.getString(getContext().getString(R.string.pref_sort_key),
                getContext().getString(R.string.pref_sort_top_rated));

        //Listen to the API results stream
        if(!sortType.equals("my_favorites")) {
            mCompositeSubscription.add(mMoviesInteractor.getMovies(sortType)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Movie>>() {
                                   @Override
                                   public void call(final List<Movie> movies) {
                                       updateMovies(movies);
                                   }
                               }, new Action1<Throwable>() {
                                   @Override
                                   public void call(final Throwable throwable) {
                                       Toast.makeText(getContext(), "Failed to fetch movies",Toast.LENGTH_SHORT).show();
                                   }
                               }
                    ));
        }

    }
}
