package org.sco.movieratings.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.sco.movieratings.api.response.MoviePreview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.db.MovieDao
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.movielist.MovieListType
import org.sco.movieratings.usecase.GetMoviePreviews
import org.sco.movieratings.usecase.GetMovieReviews
import org.sco.movieratings.usecase.GetPopularMoviesUseCase
import org.sco.movieratings.usecase.GetTopRatedMoviesUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val popularMoviesUseCase: GetPopularMoviesUseCase,
    private val topRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val moviePreviews: GetMoviePreviews,
    private val movieReviews: GetMovieReviews,
    private val movieDao: MovieDao,
) {

    fun getMovieList(listType: MovieListType): Flow<List<MovieSchema>> {
        return when (listType) {
            MovieListType.POPULAR -> getPopularMovies()
            MovieListType.TOP -> getTopRatedMovies()
            MovieListType.FAVORITE -> getFavoriteMovies()
        }
    }

    fun getMovie(movieId: Int): Flow<MovieSchema?> =
        flow {
            emit(allMovies.find { movieId == it.id })
        }.flowOn(Dispatchers.IO)

    private val popularMoviesCache: MutableList<MovieSchema> = mutableListOf()
    private val topMoviesCache: MutableList<MovieSchema> = mutableListOf()
    private val movieReviewsMap: MutableMap<Int, List<Review>> = mutableMapOf()
    private val moviePreviewsMap: MutableMap<Int, List<MoviePreview>> = mutableMapOf()
    private val allMovies: List<MovieSchema>
        get() = (popularMoviesCache + topMoviesCache)

    private fun getPopularMovies(refresh: Boolean = false): Flow<List<MovieSchema>> =
        flow {
            if (refresh || popularMoviesCache.isEmpty()) {
                popularMoviesCache.clear()
                popularMoviesCache.addAll(popularMoviesUseCase())
            }
            emit(popularMoviesCache)
        }.flowOn(Dispatchers.IO)

    private fun getTopRatedMovies(refresh: Boolean = false): Flow<List<MovieSchema>> =
        flow {
            if (refresh || topMoviesCache.isEmpty()) {
                topMoviesCache.clear()
                topMoviesCache.addAll(topRatedMoviesUseCase())
            }
            emit(topMoviesCache)
        }.flowOn(Dispatchers.IO)

    private fun getFavoriteMovies(): Flow<List<MovieSchema>> =
        movieDao.getFavorites()

    fun getMovieReviews(movieId: Int): Flow<List<Review>> =
        flow {
            if (!movieReviewsMap.containsKey(movieId)) {
                movieReviewsMap[movieId] = movieReviews.invoke(movieId)
            }
            emit(movieReviewsMap.getValue(movieId))
        }.flowOn(Dispatchers.IO)

    fun getMoviePreviews(movieId: Int): Flow<List<MoviePreview>> =
        flow {
            if (!moviePreviewsMap.containsKey(movieId)) {
                moviePreviewsMap[movieId] = moviePreviews.invoke(movieId)
            }
            emit(moviePreviewsMap.getValue(movieId))
        }.flowOn(Dispatchers.IO)

    suspend fun isFavorite(movieSchema: MovieSchema): Boolean =
        movieDao.findFavorite(movieSchema.id).firstOrNull() != null

    suspend fun addToFavorites(movieSchema: MovieSchema) {
        movieDao.addFavorite(movieSchema)
    }

    suspend fun removeFromFavorites(movie: MovieSchema) {
        movieDao.removeFavorite(movie)
    }
}