package org.sco.movieratings.shared.impl.repository

import android.util.Log
import org.sco.movieratings.db.api.MovieDao
import org.sco.movieratings.network.apiCall
import org.sco.movieratings.shared.api.MovieListItem
import org.sco.movieratings.shared.api.MovieListRepository
import org.sco.movieratings.shared.api.MoviePreviewItem
import org.sco.movieratings.shared.api.MovieReviewItem
import org.sco.movieratings.shared.impl.data.mapper.MovieMapper
import org.sco.movieratings.shared.impl.data.model.MoviesResponse
import org.sco.movieratings.shared.impl.data.remote.MovieDetailsRest
import org.sco.movieratings.shared.impl.data.remote.MovieListRest
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieListRepositoryImpl @Inject constructor(
    private val service: MovieListRest,
    private val movieDetailsRest: MovieDetailsRest,
    private val mapper: MovieMapper,
    private val movieDao: org.sco.movieratings.db.api.MovieDao
) : MovieListRepository {

    private val popularMoviesCache: MutableList<MovieListItem> = mutableListOf()
    private val topMoviesCache: MutableList<MovieListItem> = mutableListOf()
    private val allMovies: List<MovieListItem>
        get() = (popularMoviesCache + topMoviesCache)

    override suspend fun getPopularMovies(refresh: Boolean): List<MovieListItem> {
        doApiCall { service.getPopularMovies() }.apply {
            popularMoviesCache.addAll(this)
            return this
        }
    }

    override suspend fun getTopRatedMovies(refresh: Boolean): List<MovieListItem> {
        doApiCall { service.getTopRatedMovies() }.apply {
            popularMoviesCache.addAll(this)
            return this
        }
    }

    override suspend fun getFavoriteMovies(): List<MovieListItem> =
        movieDao.getFavorites().run {
            mapper.mapMovieSchemaToMovieList(this)
        }

    override suspend fun getMovie(movieId: Int): MovieListItem? {
        return allMovies.find { movieId == it.id }?.apply {
            if (this.previewList.isEmpty()) this.previewList.addAll(getMoviePreviews(this.id))
            if (this.reviewList.isEmpty()) this.reviewList.addAll(getMovieReviews(this.id))
        }
    }

    override suspend fun getMovieReviews(movieId: Int): List<MovieReviewItem> {
        val result = mutableListOf<MovieReviewItem>()
        apiCall { movieDetailsRest.getMovieReviews(movieId) }
            .onSuccess { response ->
                mapper.mapReviewList(response.reviews ?: emptyList())
                    .also {
                        result.addAll(it)
                    }
            }
            .onFailure {
                Log.d("MovieListRepository", "Api Error", it)
            }
        return result
    }

    override suspend fun getMoviePreviews(movieId: Int): List<MoviePreviewItem> {
        val result = mutableListOf<MoviePreviewItem>()
        apiCall { movieDetailsRest.getMoviePreviews(movieId) }
            .onSuccess { response ->
                mapper.mapPreviewList(response.moviePreviews ?: emptyList())
                    .also {
                        result.addAll(it)
                    }
            }
            .onFailure {
                Log.d("MovieListRepository", "Api Error", it)
            }
        return result
    }

    override suspend fun isFavorite(movieId: Int): Boolean =
        movieDao.findFavorite(movieId) != null

    override suspend fun addToFavorites(movieId: Int) {
        getMovie(movieId)?.run {
            movieDao.addFavorite(mapper.mapToMovieSchema(this))
        }
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        getMovie(movieId)?.run {
            movieDao.removeFavorite(mapper.mapToMovieSchema(this))
        }
    }

    // TODO: Maybe make this more generic?
    private suspend fun doApiCall(serviceApiCall: suspend () -> Response<MoviesResponse>): List<MovieListItem> {
        val result = mutableListOf<MovieListItem>()
        apiCall { serviceApiCall() }
            .onSuccess { response ->
                mapper.mapMovieList(response.movies ?: emptyList()).also {
                    result.addAll(it)
                }
            }
            .onFailure {
                Log.d("MovieListRepository", "Api Error", it)
            }
        return result
    }
}