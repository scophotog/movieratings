package org.sco.movieratings.interactor;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;

import org.sco.movieratings.R;
import org.sco.movieratings.activity.MainActivity;
import org.sco.movieratings.api.ApiManager;
import org.sco.movieratings.api.TheMovieDBService;
import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.api.response.MoviesResponse;
import org.sco.movieratings.db.MovieColumns;
import org.sco.movieratings.db.MovieProvider;
import org.sco.movieratings.utility.MovieListRouter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MoviesInteractor {

    private static final String LOG_TAG = MoviesInteractor.class.getSimpleName();

    private static final int RETRY_ON_ERROR_COUNT = 3;

    private final ApiManager apiManager;
    private final MovieListRouter movieListRouter;


    public MoviesInteractor(@NonNull MovieListRouter router) {
        this(new ApiManager(), router);
    }

    public MoviesInteractor(@NonNull ApiManager apiManager, @NonNull MovieListRouter router) {
        this.apiManager = apiManager;
        this.movieListRouter = router;
    }

    private static Movie movieFromCursor(Cursor c) {
        return new Movie(c.getString(c.getColumnIndex(MovieColumns.MOVIE_TITLE)), c.getInt(c.getColumnIndex(MovieColumns.MOVIE_ID)),
                c.getString(c.getColumnIndex(MovieColumns.POSTER_PATH)), c.getString(c.getColumnIndex(MovieColumns.OVERVIEW)),
                c.getString(c.getColumnIndex(MovieColumns.RELEASE_DATE)), c.getDouble(c.getColumnIndex(MovieColumns.POPULARITY)),
                c.getDouble(c.getColumnIndex(MovieColumns.RATING)));
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> emitter) {
                        try {
                            emitter.onNext(func.call());
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "Error reading from DB", e);
                        }
                    }
                });
    }

    @NonNull
    public Observable<List<Movie>> getMovies(String movieList) {
        return apiManager.getService(TheMovieDBService.class).getMovies(movieList)
                .flatMap(new Function<MoviesResponse, ObservableSource<List<Movie>>>() {
                    @Override
                    public ObservableSource<List<Movie>> apply(MoviesResponse response) {
                        if (response != null && response.getMovies() != null) {
                            return Observable.just(response.getMovies());
                        } else {
                            return Observable.error(new IllegalArgumentException("Failed to get list of movies"));
                        }
                    }
                })
                .retry(RETRY_ON_ERROR_COUNT);
    }

    @NonNull
    public Observable<List<Movie>> getFavorites(final Context context) {
        return makeObservable(getFavoriteMovies(context))
                .subscribeOn(Schedulers.computation());
    }

    private Callable<List<Movie>> getFavoriteMovies(final Context context) {
        return new Callable<List<Movie>>() {
            @Override
            public List<Movie> call() {
                List<Movie> out = new ArrayList<>();
                Cursor c = context.getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                        null,
                        MovieColumns.IS_FAVORITE + " = 1",
                        null,
                        null,
                        null);

                if (c != null && c.moveToFirst()) {
                    do {
                        out.add(movieFromCursor(c));
                    } while (c.moveToNext());
                } else {
                    Log.e(LOG_TAG, "Cursor: Null");
                }
                return out;
            }
        };
    }

    public void onMovieClicked(@NonNull Movie movie, @NonNull Context context) {
        if (((MainActivity) context).findViewById(R.id.movie_detail_container) != null) {
            movieListRouter.startFragment(movie);
        } else {
            movieListRouter.startActivity(movie, context);
        }
    }

}
