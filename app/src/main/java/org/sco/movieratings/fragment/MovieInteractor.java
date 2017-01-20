package org.sco.movieratings.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import org.sco.movieratings.api.ApiManager;
import org.sco.movieratings.api.TheMovieDBService;
import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.api.models.Preview;
import org.sco.movieratings.api.models.Review;
import org.sco.movieratings.api.response.MoviesResponse;
import org.sco.movieratings.api.response.PreviewsResponse;
import org.sco.movieratings.api.response.ReviewsResponse;
import org.sco.movieratings.data.MovieColumns;
import org.sco.movieratings.data.MovieProvider;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sargenzi on 1/19/17.
 */

public class MovieInteractor {

    private static final String LOG_TAG = MovieInteractor.class.getSimpleName();

    private static final int RETRY_ON_ERROR_COUNT = 3;

    private final ApiManager apiManager;

    public MovieInteractor() {
        this(new ApiManager());
    }

    public MovieInteractor(@NonNull ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    @NonNull
    public Observable<List<Review>> getReviews(Movie movie) {
        return apiManager.getService(TheMovieDBService.class).getMovieReviews(movie.getId())
                .flatMap(new Func1<ReviewsResponse, Observable<List<Review>>>() {
                    @Override
                    public Observable<List<Review>> call(final ReviewsResponse response) {
                        if (response != null && response.getReviews() != null) {
                            return Observable.just(response.getReviews());
                        } else {
                            return Observable.error(new IllegalArgumentException("Failed to get list of reviews"));
                        }
                    }
                })
                .retry(RETRY_ON_ERROR_COUNT);
    }

    @NonNull
    public Observable<List<Preview>> getPreviews(Movie movie) {
        return apiManager.getService(TheMovieDBService.class).getMoviePreviews(movie.getId())
                .flatMap(new Func1<PreviewsResponse, Observable<List<Preview>>>() {
                    @Override
                    public Observable<List<Preview>> call(final PreviewsResponse response) {
                        if (response != null && response.getPreviews() != null) {
                            return Observable.just(response.getPreviews());
                        } else {
                            return Observable.error(new IllegalArgumentException("Failed to get list of reviews"));
                        }
                    }
                })
                .retry(RETRY_ON_ERROR_COUNT);
    }

}