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
import javax.inject.Singleton

@Singleton
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

    private val popularMoviesCache: MutableList<MovieSchema> = mutableListOf()
    private val topMoviesCache: MutableList<MovieSchema> = mutableListOf()
    private val movieReviewsMap: MutableMap<Int, List<Review>> = mutableMapOf()
    private val moviePreviewsMap: MutableMap<Int, List<Preview>> = mutableMapOf()

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

    fun getMoviePreviews(movieId: Int): Flow<List<Preview>> =
        flow {
            if (!moviePreviewsMap.containsKey(movieId)) {
                moviePreviewsMap[movieId] = moviePreviews.invoke(movieId)
            }
            emit(moviePreviewsMap.getValue(movieId))
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