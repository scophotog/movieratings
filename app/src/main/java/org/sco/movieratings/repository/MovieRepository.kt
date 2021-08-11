package org.sco.movieratings.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.sco.movieratings.api.response.Preview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.usecase.GetMoviePreviews
import org.sco.movieratings.usecase.GetMovieReviews
import org.sco.movieratings.usecase.GetPopularMoviesUseCase
import org.sco.movieratings.usecase.GetTopRatedMoviesUseCase
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val popularMoviesUseCase: GetPopularMoviesUseCase,
    private val topRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val moviePreviews: GetMoviePreviews,
    private val movieReviews: GetMovieReviews
) {
    suspend fun getPopularMovies(): Flow<List<MovieSchema>> =
        flow {
            val movies = popularMoviesUseCase.invoke()
            if (movies.isSuccess) {
                emit(movies.getOrDefault(emptyList()))
            }
        }

    suspend fun getTopRatedMovies(): Flow<List<MovieSchema>> =
        flow {
            val movies = topRatedMoviesUseCase.invoke()
            if (movies.isSuccess) {
                emit(movies.getOrDefault(emptyList()))
            }
        }

    suspend fun getMovieReviews(movieId: Int): Flow<List<Review>> =
        flow {
            val reviews = movieReviews.invoke(movieId)
            if (reviews.isSuccess) {
                emit(reviews.getOrDefault(emptyList()))
            }
        }

    suspend fun getMoviePreviews(movieId: Int): Flow<List<Preview>> =
        flow {
            val previews = moviePreviews.invoke(movieId)
            if (previews.isSuccess) {
                emit(previews.getOrDefault(emptyList()))
            }
        }
}