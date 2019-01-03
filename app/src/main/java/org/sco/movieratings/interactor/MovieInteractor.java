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
import io.reactivex.Observable;
import io.reactivex.functions.Function;

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
                .flatMap(new Function<ReviewsResponse, Observable<List<Review>>>() {
                    @Override
                    public Observable<List<Review>> apply(ReviewsResponse response) {
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
                .flatMap(new Function<PreviewsResponse, Observable<List<Preview>>>() {
                    @Override
                    public Observable<List<Preview>> apply(PreviewsResponse response) throws Exception {
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
