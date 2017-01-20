package org.sco.movieratings.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.sco.movieratings.utility.MovieListRouter;
import org.sco.movieratings.R;
import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.interactor.MoviesInteractor;
import org.sco.movieratings.presenter.MovieListPresenter;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * The activity for displaying all movie posters.
 */

public class MovieListFragment extends Fragment {

    private static final String LOG_TAG = MovieListFragment.class.getSimpleName();

    private static final String SAVED_MOVIES = "movies";
    private MoviesInteractor mMoviesInteractor;
    private MovieListPresenter mMovieListPresenter;
    private CompositeSubscription mCompositeSubscription;

    List<Movie> mMovies;

    public MovieListFragment() {
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoviesInteractor = new MoviesInteractor(new MovieListRouter(getFragmentManager()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMovieListPresenter = new MovieListPresenter(view);

        mMovies = new ArrayList<>();
        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(SAVED_MOVIES);
            mMovieListPresenter.present(mMovies);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_MOVIES,
                (ArrayList<? extends Parcelable>) mMovieListPresenter.getMovies());
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
                                       mMovieListPresenter.present(movies);
                                   }
                               }, new Action1<Throwable>() {
                                   @Override
                                   public void call(final Throwable throwable) {
                                       Toast.makeText(getContext(), "Failed to fetch movies",Toast.LENGTH_SHORT).show();
                                   }
                               }
                    ));
        } else {
            mCompositeSubscription.add(mMoviesInteractor.getFavorites(getContext())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Movie>>() {
                            @Override
                            public void call(final List<Movie> movies) {
                                mMovieListPresenter.present(movies);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(final Throwable throwable) {
                                Toast.makeText(getContext(), "Failed to fetch movies", Toast.LENGTH_SHORT).show();
                            }
                        }
                    ));
        }

        mCompositeSubscription.add(mMovieListPresenter.getMovieClickStream()
                .subscribe(new Action1<Movie>() {
                    @Override
                    public void call(final Movie movie) {
                        mMoviesInteractor.onMovieClicked(movie, getContext());
                    }
                }));
    }

    @Override
    public void onPause() {
        mCompositeSubscription.unsubscribe();
        super.onPause();
    }

}
