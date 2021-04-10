package org.sco.movieratings.repository

import kotlinx.coroutines.flow.*
import org.sco.movieratings.api.MoviesService
import org.sco.movieratings.db.MovieDao
import org.sco.movieratings.db.MovieSchema
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val service: MoviesService,
    private val mapper: MovieMapper,
    private val movieDao: MovieDao
) {
    suspend fun getPopularMovies(): Flow<Result<List<MovieSchema>>> =
        service.getPopularMovies().map {
            if (it.isSuccess) {
                Result.success(mapper(it.getOrDefault(emptyList())))
            } else {
                Result.failure(it.exceptionOrNull()?: throw Exception("Ooops"))
            }
        }

    suspend fun getTopRatedMovies(): Flow<Result<List<MovieSchema>>> =
        service.getTopRatedMovies().map {
            if (it.isSuccess && it.getOrNull() != null) {
                Result.success(mapper(it.getOrDefault(emptyList())))
            } else {
                Result.failure(it.exceptionOrNull()?: throw Exception("Ooops"))
            }
        }

    fun getFavoriteMovies() : Flow<List<MovieSchema>> =
        movieDao.getFavorites()

    fun isFavorite(movieSchema: MovieSchema) : Flow<MovieSchema?> =
        movieDao.findFavorite(movieSchema.id)

    suspend fun addToFavorites(movieSchema: MovieSchema) =
        movieDao.addFavorite(movieSchema)

    suspend fun removeFromFavorites(movie: MovieSchema) =
        movieDao.removeFavorite(movie)

}