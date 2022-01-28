package org.sco.movieratings.api

import org.sco.movieratings.api.response.MoviesResponse
import org.sco.movieratings.api.response.PreviewsResponse
import org.sco.movieratings.api.response.ReviewsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheMovieDBService @Inject constructor(private val movieApi: TheMovieDBApi) {

    suspend fun getPopularMovies(): Result<MoviesResponse> {
        return apiCall { movieApi.getPopularMovies() }
    }

    suspend fun getTopRatedMovies(): Result<MoviesResponse> {
        return apiCall { movieApi.getTopRatedMovies() }
    }

    suspend fun getMoviePreviews(movieId: Int): Result<PreviewsResponse> {
        return apiCall { movieApi.getMoviePreviews(movieId) }
    }

    suspend fun getMovieReviews(movieId: Int): Result<ReviewsResponse> {
        return apiCall { movieApi.getMovieReviews(movieId) }
    }
}