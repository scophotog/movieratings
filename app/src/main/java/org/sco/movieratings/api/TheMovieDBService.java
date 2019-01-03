package org.sco.movieratings.api;

import org.sco.movieratings.api.response.MoviesResponse;
import org.sco.movieratings.api.response.PreviewsResponse;
import org.sco.movieratings.api.response.ReviewsResponse;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TheMovieDBService {

    @GET("movie/{list}")
    Observable<MoviesResponse> getMovies(@Path("list") @NonNull String list);

    @GET("movie/{id}/videos")
    Observable<PreviewsResponse> getMoviePreviews(@Path("id") @NonNull int movieId);

    @GET("movie/{id}/reviews")
    Observable<ReviewsResponse> getMovieReviews(@Path("id") @NonNull int movieId);

}
