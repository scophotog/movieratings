package org.sco.movieratings.api

import org.sco.movieratings.api.response.MoviesResponse
import org.sco.movieratings.api.response.PreviewsResponse
import org.sco.movieratings.api.response.ReviewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBApi {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    @GET("movie/{list}")
    suspend fun getMovies(@Path("list") list: String): Response<MoviesResponse?>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): Response<MoviesResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<MoviesResponse>

    @GET("movie/{id}/videos")
    suspend fun getMoviePreviews(@Path("id") movieId: Int): Response<PreviewsResponse>

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(@Path("id") movieId: Int): Response<ReviewsResponse>
}