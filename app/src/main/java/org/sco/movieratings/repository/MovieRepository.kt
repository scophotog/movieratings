package org.sco.movieratings.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.sco.movieratings.api.response.Preview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.db.MovieDao
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.movielist.MovieListViewModel
import org.sco.movieratings.usecase.GetMoviePreviews
import org.sco.movieratings.usecase.GetMovieReviews
import org.sco.movieratings.usecase.GetPopularMoviesUseCase
import org.sco.movieratings.usecase.GetTopRatedMoviesUseCase
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val popularMoviesUseCase: GetPopularMoviesUseCase,
    private val topRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val moviePreviews: GetMoviePreviews,
    private val movieReviews: GetMovieReviews,
    private val movieDao: MovieDao,
) {

    fun getMovieList(listType: MovieListViewModel.MovieListType): Flow<List<MovieSchema>> {
        return when (listType) {
            MovieListViewModel.MovieListType.POPULAR -> getPopularMovies()
            MovieListViewModel.MovieListType.TOP -> getTopRatedMovies()
            MovieListViewModel.MovieListType.FAVORITE -> getFavoriteMovies()
        }
    }

    private fun getPopularMovies(): Flow<List<MovieSchema>> =
        flow {
            popularMoviesUseCase.invoke()
                .onSuccess { movies -> emit(movies) }
                .onFailure { e -> throw IllegalStateException("Error getting popular movies", e) }
        }.flowOn(Dispatchers.IO)

    private fun getTopRatedMovies(): Flow<List<MovieSchema>> =
        flow {
            topRatedMoviesUseCase.invoke()
                .onSuccess { movies -> emit(movies) }
                .onFailure { e -> throw IllegalStateException("Error getting top movies", e) }
        }.flowOn(Dispatchers.IO)

    private fun getFavoriteMovies(): Flow<List<MovieSchema>> =
        movieDao.getFavorites()

    fun getMovieReviews(movieId: Int): Flow<List<Review>> =
        flow {
            movieReviews.invoke(movieId)
                .onSuccess { reviews -> emit(reviews) }
                .onFailure { e -> throw IllegalStateException("Error getting reviews", e) }
        }.flowOn(Dispatchers.IO)

    fun getMoviePreviews(movieId: Int): Flow<List<Preview>> =
        flow {
            moviePreviews.invoke(movieId)
                .onSuccess { previews -> emit(previews) }
                .onFailure { e -> throw IllegalStateException("Error getting previews", e) }
        }.flowOn(Dispatchers.IO)


    fun isFavorite(movieSchema: MovieSchema): Flow<Boolean> =
        flow {
            emit(movieDao.findFavorite(movieSchema.id).firstOrNull() != null)
        }.flowOn(Dispatchers.IO)

    suspend fun addToFavorites(movieSchema: MovieSchema) {
        movieDao.addFavorite(movieSchema)
    }

    suspend fun removeFromFavorites(movie: MovieSchema) {
        movieDao.removeFavorite(movie)
    }
}