package org.sco.movieratings.movielist.data.remote

import org.sco.movieratings.movielist.data.model.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET

interface MovieListRest {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): Response<MoviesResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<MoviesResponse>
}