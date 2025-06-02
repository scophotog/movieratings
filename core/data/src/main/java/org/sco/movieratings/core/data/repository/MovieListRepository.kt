package org.sco.movieratings.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.sco.movieratings.core.model.data.MovieListItem

interface MovieListRepository {

    suspend fun getFavoriteMovies(): Flow<List<MovieListItem>>
    suspend fun getPopularMovies(): Flow<List<MovieListItem>>
    suspend fun getTopRatedMovies(): Flow<List<MovieListItem>>
    suspend fun getMovie(movieId: Int): Flow<MovieListItem?>

    suspend fun isFavorite(movieId: Int): Flow<Boolean>
    suspend fun addToFavorites(movieId: Int)
    suspend fun removeFromFavorites(movieId: Int)
}