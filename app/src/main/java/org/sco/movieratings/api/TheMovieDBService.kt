package org.sco.movieratings.api

import org.sco.movieratings.api.response.MoviesResponse
import org.sco.movieratings.api.response.PreviewsResponse
import org.sco.movieratings.api.response.ReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBService {

    @GET("movie/{list}")
    suspend fun getMovies(@Path("list") list: String): MoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): MoviesResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(): MoviesResponse

    @GET("movie/{id}/videos")
    suspend fun getMoviePreviews(@Path("id") movieId: Int): PreviewsResponse

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(@Path("id") movieId: Int): ReviewsResponse

}