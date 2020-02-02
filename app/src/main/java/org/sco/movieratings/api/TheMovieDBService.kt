package org.sco.movieratings.api

import io.reactivex.Observable
import org.sco.movieratings.api.response.MoviesResponse
import org.sco.movieratings.api.response.PreviewsResponse
import org.sco.movieratings.api.response.ReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBService {

    @GET("movie/{list}")
    fun getMovies(@Path("list") list: String): Observable<MoviesResponse>

    @GET("movie/{id}/videos")
    fun getMoviePreviews(@Path("id") movieId: Int): Observable<PreviewsResponse>

    @GET("movie/{id}/reviews")
    fun getMovieReviews(@Path("id") movieId: Int): Observable<ReviewsResponse>

}