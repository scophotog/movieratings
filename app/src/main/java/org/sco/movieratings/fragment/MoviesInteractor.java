package org.sco.movieratings.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import org.sco.movieratings.R;
import org.sco.movieratings.activity.MainActivity;
import org.sco.movieratings.api.ApiManager;
import org.sco.movieratings.api.response.MoviesResponse;
import org.sco.movieratings.api.TheMovieDBService;
import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.data.MovieColumns;
import org.sco.movieratings.data.MovieProvider;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sargenzi on 1/19/17.
 */

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

    @NonNull
    public Observable<List<Movie>> getMovies(String movieList) {
        return apiManager.getService(TheMovieDBService.class).getMovies(movieList)
                .flatMap(new Func1<MoviesResponse, Observable<List<Movie>>>() {
                    @Override
                    public Observable<List<Movie>> call(final MoviesResponse response) {
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
                        out.add(Movie.fromCursor(c));
                    } while (c.moveToNext());
                } else {
                    Log.e(LOG_TAG, "Cursor: Null");
                }
                return out;
            }
        };
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch(Exception e) {
                            Log.e(LOG_TAG, "Error reading from DB", e);
                        }
                    }
                });
    }

    public void onMovieClicked(@NonNull Movie movie, @NonNull Context context) {
         if (((MainActivity) context).findViewById(R.id.movie_detail_container) != null) {
             movieListRouter.startFragment(movie);
         } else {
             movieListRouter.startActivity(movie, context);
         }
    }

}
