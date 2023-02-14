package org.sco.movieratings.repository

import kotlinx.coroutines.flow.Flow
import org.sco.movieratings.api.response.MoviePreview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.movielist.MovieListType

interface IMovieRepository {
    fun getMovieList(listType: MovieListType): Flow<List<MovieSchema>>
    fun getMovie(movieId: Int): Flow<MovieSchema?>
    fun getPopularMovies(refresh: Boolean = false): Flow<List<MovieSchema>>
    fun getTopRatedMovies(refresh: Boolean = false): Flow<List<MovieSchema>>
    fun getFavoriteMovies(): Flow<List<MovieSchema>>
    fun getMovieReviews(movieId: Int): Flow<List<Review>>
    fun getMoviePreviews(movieId: Int): Flow<List<MoviePreview>>

    suspend fun isFavorite(movieSchema: MovieSchema): Boolean

    suspend fun addToFavorites(movieSchema: MovieSchema)

    suspend fun removeFromFavorites(movie: MovieSchema)
}