package org.sco.movieratings.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.sco.movieratings.core.data.model.toMovieSchema
import org.sco.movieratings.core.model.data.MovieListItem
import org.sco.movieratings.db.dao.MovieDao
import org.sco.movieratings.db.model.toMovieListItem
import org.sco.movieratings.network.TheMovieDbNetworkDataSource
import org.sco.movieratings.network.model.toMovieListItem
import org.sco.movieratings.network.model.toMoviePreviewItem
import org.sco.movieratings.network.model.toReviewItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieListRepositoryImpl @Inject constructor(
    private val service: TheMovieDbNetworkDataSource,
    private val movieDao: MovieDao
) : MovieListRepository {

    private val popularMoviesCache: MutableList<MovieListItem> = mutableListOf()
    private val topMoviesCache: MutableList<MovieListItem> = mutableListOf()
    private val allMovies: List<MovieListItem>
        get() = (popularMoviesCache + topMoviesCache)

    override suspend fun getPopularMovies(): Flow<List<MovieListItem>> {
        // TODO: Implement retrieving from cache
        service.getPopularMovies().apply {
            this.mapTo(popularMoviesCache) { it.toMovieListItem() }
        }
        return flowOf(popularMoviesCache)
    }

    override suspend fun getTopRatedMovies(): Flow<List<MovieListItem>> {
        // TODO: Implement retrieving from cache
        service.getTopRatedMovies().apply {
            this.mapTo(topMoviesCache) { it.toMovieListItem() }
        }
        return flowOf(topMoviesCache)
    }

    override suspend fun getMovie(movieId: Int): Flow<MovieListItem?> {
        val movie = allMovies.find { movieId == it.id }?.apply {
            if (this.previewList.isEmpty()) this.previewList.addAll(
                service.getMoviePreviews(
                    this.id
                ).map { it.toMoviePreviewItem() })
            if (this.reviewList.isEmpty()) this.reviewList.addAll(
                service.getMovieReviews(this.id).map { it.toReviewItem() })
        }
        return flowOf(movie)
    }

    override suspend fun isFavorite(movieId: Int): Flow<Boolean> =
        flowOf(movieDao.findFavorite(movieId).firstOrNull() != null)

    override suspend fun getFavoriteMovies(): Flow<List<MovieListItem>> =
        movieDao.getFavorites().map { movieItems ->
            movieItems.map { it.toMovieListItem() }
        }

    override suspend fun addToFavorites(movieId: Int) {
        getMovie(movieId).collect {
            it?.let { movie -> movieDao.addFavorite(movie.toMovieSchema())}
        }
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        getMovie(movieId).collect {
            it?.let { movie -> movieDao.removeFavorite(movie.toMovieSchema())}
        }
    }
}