package org.sco.movieratings.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.sco.movieratings.db.MovieDao
import org.sco.movieratings.db.MovieSchema
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val coroutineScope: CoroutineScope
){

    fun getFavoriteMovies() : Flow<List<MovieSchema>> =
        movieDao.getFavorites()

    fun isFavorite(movieSchema: MovieSchema) : Flow<MovieSchema?> =
        movieDao.findFavorite(movieSchema.id)

    suspend fun addToFavorites(movieSchema: MovieSchema) {
        coroutineScope.launch {
            movieDao.addFavorite(movieSchema)
        }
    }

    suspend fun removeFromFavorites(movie: MovieSchema) {
        coroutineScope.launch {
            movieDao.removeFavorite(movie)
        }
    }
}