package org.sco.movieratings.interactor;

import java.util.List;

import org.sco.movieratings.api.ApiManager;
import org.sco.movieratings.api.TheMovieDBService;
import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.api.models.Preview;
import org.sco.movieratings.api.models.Review;
import org.sco.movieratings.api.response.PreviewsResponse;
import org.sco.movieratings.api.response.ReviewsResponse;

import androidx.annotation.NonNull;
import rx.Observable;
import rx.functions.Func1;

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
