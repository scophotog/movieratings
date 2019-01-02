package org.sco.movieratings.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.sco.movieratings.R;
import org.sco.movieratings.activity.MainActivity;
import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.interactor.MoviesInteractor;
import org.sco.movieratings.presenter.BottomBarPresenter;
import org.sco.movieratings.presenter.MovieListPresenter;
import org.sco.movieratings.utility.MovieListRouter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static org.sco.movieratings.utility.Utility.getPreferredSort;
import static org.sco.movieratings.utility.Utility.updatePreference;

/**
 * The activity for displaying all movie posters.
 */
public class MovieListFragment extends Fragment {

    private static final String LOG_TAG = MovieListFragment.class.getSimpleName();

    private static final String SAVED_MOVIES = "movies";
    private MoviesInteractor mMoviesInteractor;
    private MovieListPresenter mMovieListPresenter;
    private BottomBarPresenter mBottomBarPresenter;
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
        mBottomBarPresenter = new BottomBarPresenter(view);
        mBottomBarPresenter.setOnNavigationItemSelectedListener(setBottomNavListener());
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
        final String sortType = getPreferredSort(getContext());
        mCompositeSubscription = new CompositeSubscription();
        setViewToSortType(mCompositeSubscription, sortType);
        setMovieClickInteractor(mCompositeSubscription);
    }

    @Override
    public void onPause() {
        mCompositeSubscription.unsubscribe();
        super.onPause();
    }

    private void setViewToSortType(final CompositeSubscription compositeSubscription, final String sortType) {
        final String topRated = getResources().getString(R.string.pref_sort_top_rated);
        final String popular = getResources().getString(R.string.pref_sort_popular_rated);
        final String favorites = getResources().getString(R.string.pref_sort_my_favorites);
        if (sortType.equals(topRated)) {
            updateMovieList(compositeSubscription, 0);
            mBottomBarPresenter.setSelectedItemById(R.id.bn_top_rated);
        } else if (sortType.equals(popular)) {
            updateMovieList(compositeSubscription, 1);
            mBottomBarPresenter.setSelectedItemById(R.id.bn_most_popular);
        } else if (sortType.equals(favorites)) {
            updateMovieList(compositeSubscription, 2);
            mBottomBarPresenter.setSelectedItemById(R.id.bn_my_favorites);
        } else {
            throw new IllegalArgumentException("Unknown navigation: " + sortType);
        }
    }

    private void setMovieClickInteractor(final CompositeSubscription compositeSubscription) {
        compositeSubscription.add(mMovieListPresenter.getMovieClickStream()
                .subscribe(new Action1<Movie>() {
                    @Override
                    public void call(final Movie movie) {
                        mMoviesInteractor.onMovieClicked(movie, getContext());
                    }
                }));
    }

    private void updateMovieList(final CompositeSubscription compositeSubscription, int position) {
        mMovieListPresenter.setNowLoadingView();
        switch(position) {
            case 0: {
                compositeSubscription.add(mMoviesInteractor.getMovies("top_rated")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<Movie>>() {
                                       @Override
                                       public void call(final List<Movie> movies) {
                                           mMovieListPresenter.present(movies);
                                       }
                                   }, new Action1<Throwable>() {
                                       @Override
                                       public void call(final Throwable throwable) {
                                           mMovieListPresenter.setErrorView();
                                       }
                                   }
                        ));
                ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.high_rated_settings));
                updatePreference(getContext(),
                        getResources().getString(R.string.pref_sort_top_rated));
                break;
            }
            case 1: {
                compositeSubscription.add(mMoviesInteractor.getMovies("popular")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<Movie>>() {
                                       @Override
                                       public void call(final List<Movie> movies) {
                                           mMovieListPresenter.present(movies);
                                       }
                                   }, new Action1<Throwable>() {
                                       @Override
                                       public void call(final Throwable throwable) {
                                           mMovieListPresenter.setErrorView();
                                       }
                                   }
                        ));
                ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.most_popular_settings));
                updatePreference(getContext(),
                        getResources().getString(R.string.pref_sort_popular_rated));
                break;
            }
            case 2: {
                compositeSubscription.add(mMoviesInteractor.getFavorites(getContext())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<Movie>>() {
                                       @Override
                                       public void call(final List<Movie> movies) {
                                           mMovieListPresenter.present(movies);
                                       }
                                   }, new Action1<Throwable>() {
                                       @Override
                                       public void call(final Throwable throwable) {
                                           mMovieListPresenter.setErrorView();
                                       }
                                   }
                        ));
                ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.my_favorites_settings));
                updatePreference(getContext(),
                        getResources().getString(R.string.pref_sort_my_favorites));
                break;
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener setBottomNavListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bn_top_rated: {
                        updateMovieList(mCompositeSubscription, 0);
                        break;
                    }
                    case R.id.bn_most_popular: {
                        updateMovieList(mCompositeSubscription,1);
                        break;
                    }
                    case R.id.bn_my_favorites: {
                        updateMovieList(mCompositeSubscription,2);
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unknown navigation");
                    }
                }
                return true;
            }
        };
    }

}
