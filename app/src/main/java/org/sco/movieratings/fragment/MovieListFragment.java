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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

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
    private CompositeDisposable compositeDisposable;

    private List<Movie> mMovies;

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
        compositeDisposable = new CompositeDisposable();
        setViewToSortType(compositeDisposable, sortType);
        setMovieClickInteractor(compositeDisposable);
    }

    @Override
    public void onPause() {
        compositeDisposable.clear();
        super.onPause();
    }

    private void setViewToSortType(final CompositeDisposable compositeDisposable, final String sortType) {
        final String topRated = getResources().getString(R.string.pref_sort_top_rated);
        final String popular = getResources().getString(R.string.pref_sort_popular_rated);
        final String favorites = getResources().getString(R.string.pref_sort_my_favorites);
        if (sortType.equals(topRated)) {
            updateMovieList(compositeDisposable, 0);
            mBottomBarPresenter.setSelectedItemById(R.id.bn_top_rated);
        } else if (sortType.equals(popular)) {
            updateMovieList(compositeDisposable, 1);
            mBottomBarPresenter.setSelectedItemById(R.id.bn_most_popular);
        } else if (sortType.equals(favorites)) {
            updateMovieList(compositeDisposable, 2);
            mBottomBarPresenter.setSelectedItemById(R.id.bn_my_favorites);
        } else {
            throw new IllegalArgumentException("Unknown navigation: " + sortType);
        }
    }

    private void setMovieClickInteractor(final CompositeDisposable compositeDisposable) {
        compositeDisposable.add(mMovieListPresenter.getMovieClickStream()
                .subscribe(new Consumer<Movie>() {
                    @Override
                    public void accept(final Movie movie) {
                        mMoviesInteractor.onMovieClicked(movie, getContext());
                    }
                }));
    }

    private void updateMovieList(final CompositeDisposable compositeDisposable, int position) {
        mMovieListPresenter.setNowLoadingView();
        switch(position) {
            case 0: {
                compositeDisposable.add(mMoviesInteractor.getMovies("top_rated")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Movie>>() {
                                       @Override
                                       public void accept(final List<Movie> movies) {
                                           mMovieListPresenter.present(movies);
                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(final Throwable throwable) {
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
                compositeDisposable.add(mMoviesInteractor.getMovies("popular")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Movie>>() {
                                       @Override
                                       public void accept(final List<Movie> movies) {
                                           mMovieListPresenter.present(movies);
                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(final Throwable throwable) {
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
                compositeDisposable.add(mMoviesInteractor.getFavorites(getContext())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Movie>>() {
                                       @Override
                                       public void accept(final List<Movie> movies) {
                                           mMovieListPresenter.present(movies);
                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(final Throwable throwable) {
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
                        updateMovieList(compositeDisposable, 0);
                        break;
                    }
                    case R.id.bn_most_popular: {
                        updateMovieList(compositeDisposable,1);
                        break;
                    }
                    case R.id.bn_my_favorites: {
                        updateMovieList(compositeDisposable,2);
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
