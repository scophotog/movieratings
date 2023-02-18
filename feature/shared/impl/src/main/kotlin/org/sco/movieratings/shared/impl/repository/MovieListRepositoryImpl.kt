package org.sco.movieratings.shared.impl.repository

import android.util.Log
import okhttp3.internal.toImmutableList
import org.sco.movieratings.db.MovieDao
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.shared.api.MovieListRepository
import org.sco.movieratings.network.apiCall
import org.sco.movieratings.shared.api.MovieListItem
import org.sco.movieratings.shared.api.MoviePreviewItem
import org.sco.movieratings.shared.api.MovieReviewItem
import org.sco.movieratings.shared.impl.data.mapper.MovieMapper
import org.sco.movieratings.shared.impl.data.mapper.MovieSchemaMapper
import org.sco.movieratings.shared.impl.data.model.MoviesResponse
import org.sco.movieratings.shared.impl.data.remote.MovieDetailsRest
import org.sco.movieratings.shared.impl.data.remote.MovieListRest
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MovieListRepositoryImpl @Inject constructor(
    private val service: MovieListRest,
    private val movieDetailsRest: MovieDetailsRest,
    private val mapper: MovieMapper,
    private val movieDao: MovieDao,
    private val movieSchemaMapper: MovieSchemaMapper
) : MovieListRepository {

    private val popularMoviesCache: MutableList<MovieListItem> = mutableListOf()
    private val topMoviesCache: MutableList<MovieListItem> = mutableListOf()
    private val movieReviewsMap: MutableMap<Int, List<MovieReviewItem>> = mutableMapOf()
    private val moviePreviewsMap: MutableMap<Int, List<MoviePreviewItem>> = mutableMapOf()
    private val allMovies: List<MovieListItem>
        get() = (popularMoviesCache + topMoviesCache)

    override suspend fun getPopularMovies(refresh: Boolean): List<MovieListItem> {
        doApiCall{ service.getPopularMovies() }.apply {
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
            movieSchemaMapper(this)
        }

    override suspend fun getMovie(movieId: Int): MovieListItem? =
        allMovies.find { movieId == it.id }

    override suspend fun getMovieReviews(movieId: Int): List<MovieReviewItem> {
        val result = mutableListOf<MovieReviewItem>()
        apiCall { movieDetailsRest.getMovieReviews(movieId) }
            .onSuccess { response ->
                response.reviews?.map {
                    MovieReviewItem(
                        id = it.id,
                        author = it.author,
                        url = it.url,
                        content = it.content
                    )
                }.also {
                    result.addAll(it ?: emptyList())
                }
            }
            .onFailure {
                Log.d("MovieListRepository", "Api Error", it)
            }
        return result
    }

//    =
//        doApiCall { movieDetailsRest.getMovieReviews(movieId) }

    override suspend fun getMoviePreviews(movieId: Int): List<MoviePreviewItem> {
        val result = mutableListOf<MoviePreviewItem>()
        apiCall { movieDetailsRest.getMoviePreviews(movieId) }
            .onSuccess { response ->
                response.moviePreviews?.map {
                    MoviePreviewItem(
                        id = it.id,
                        key = it.key,
                        name = it.name,
                        site = it.site,
                        size = it.size,
                        type = it.type,
                        iso_3166_1 = it.iso_3166_1,
                        iso_6391 = it.iso_6391
                    )
                }.also {
                    result.addAll(it ?: emptyList())
                }
            }
            .onFailure {
                Log.d("MovieListRepository", "Api Error", it)
            }
        return result
    }

//    =
//        doApiCall { movieDetailsRest.getMoviePreviews(movieId) }

    override suspend fun isFavorite(movieSchema: MovieListItem): Boolean =
        movieDao.findFavorite(movieSchema.id) != null

    override suspend fun addToFavorites(movieSchema: MovieListItem) {
        movieDao.addFavorite(movieSchema.toMovieSchema())
    }

    override suspend fun removeFromFavorites(movie: MovieListItem) {
        movieDao.removeFavorite(movie.toMovieSchema())
    }

    private fun MovieListItem.toMovieSchema(): MovieSchema {
        return MovieSchema(
            id = id,
            title = title,
            posterPath = posterPath ?: "",
            overview = overview ?: "",
            releaseDate = releaseDate ?: "",
            popularity = popularity ?: 0.0,
            voteAverage = voteAverage?.toFloat() ?: 0.0f,
            backdropPath = backdropPath ?: ""
        )
    }
//    private suspend fun <R : Any, T : Collection<Any>> doApiCall(serviceApiCall: suspend () -> Response<R>): List<Any> {
//        val result = mutableListOf<Any>()
//        apiCall { serviceApiCall() }
//            .onSuccess {response ->
//                when(response) {
//                    is MoviesResponse -> {
//                        mapper(response.movies ?: emptyList()).also {
//                            result.addAll(it)
//                        }
//                    }
//                }
//            }
//            .onFailure {
//                // Womp womp
//            }
//        return result
//    }

    private suspend fun doApiCall(serviceApiCall: suspend () -> Response<MoviesResponse>): List<MovieListItem> {
        val result = mutableListOf<MovieListItem>()
        apiCall { serviceApiCall() }
            .onSuccess { response ->
                mapper(response.movies ?: emptyList()).also {
                    result.addAll(it)
                }
            }
            .onFailure {
                Log.d("MovieListRepository", "Api Error", it)
            }
        return result
    }
}