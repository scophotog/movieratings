package org.sco.movieratings.fragment;

import java.util.List;

import android.support.annotation.NonNull;

import org.sco.movieratings.api.ApiManager;
import org.sco.movieratings.api.response.MoviesResponse;
import org.sco.movieratings.api.response.PreviewsResponse;
import org.sco.movieratings.api.response.ReviewsResponse;
import org.sco.movieratings.api.TheMovieDBService;
import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.api.models.Preview;
import org.sco.movieratings.api.models.Review;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sargenzi on 1/19/17.
 */

public class MoviesInteractor {

    private static final int RETRY_ON_ERROR_COUNT = 3;

    private final ApiManager apiManager;

    public MoviesInteractor() {
        this(new ApiManager());
    }

    public MoviesInteractor(@NonNull ApiManager apiManager) {
        this.apiManager = apiManager;
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
