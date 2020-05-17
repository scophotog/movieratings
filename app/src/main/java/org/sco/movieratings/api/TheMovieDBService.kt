package org.sco.movieratings.api

import io.reactivex.Observable
import io.reactivex.Single
import org.sco.movieratings.api.response.MoviesResponse
import org.sco.movieratings.api.response.PreviewsResponse
import org.sco.movieratings.api.response.ReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBService {

    @GET("movie/{list}")
    fun getMovies(@Path("list") list: String): Observable<MoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(): Single<MoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(): Single<MoviesResponse>

    @GET("movie/{id}/videos")
    fun getMoviePreviews(@Path("id") movieId: Int): Single<PreviewsResponse>

    @GET("movie/{id}/reviews")
    fun getMovieReviews(@Path("id") movieId: Int): Single<ReviewsResponse>

}