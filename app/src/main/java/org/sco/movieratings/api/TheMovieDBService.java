package org.sco.movieratings.api;

import org.sco.movieratings.api.response.MoviesResponse;
import org.sco.movieratings.api.response.PreviewsResponse;
import org.sco.movieratings.api.response.ReviewsResponse;

import androidx.annotation.NonNull;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by sargenzi on 1/18/17.
 */

public interface TheMovieDBService {

    @GET("movie/{list}")
    Observable<MoviesResponse> getMovies(@Path("list") @NonNull String list);

    @GET("movie/{id}/videos")
    Observable<PreviewsResponse> getMoviePreviews(@Path("id") @NonNull int movieId);

    @GET("movie/{id}/reviews")
    Observable<ReviewsResponse> getMovieReviews(@Path("id") @NonNull int movieId);

}
