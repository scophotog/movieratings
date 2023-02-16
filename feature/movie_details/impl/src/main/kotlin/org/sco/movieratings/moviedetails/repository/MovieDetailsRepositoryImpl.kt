package org.sco.movieratings.moviedetails.repository

import org.sco.movieratings.db.MovieDao
import org.sco.movieratings.moviedetails.api.MovieDetailItem
import org.sco.movieratings.moviedetails.api.MovieDetailsRepository
import org.sco.movieratings.moviedetails.api.MoviePreviewItem
import org.sco.movieratings.moviedetails.api.MovieReviewItem
import org.sco.movieratings.moviedetails.data.remote.MovieDetailsRest
import org.sco.movieratings.network.apiCall
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MovieDetailsRepositoryImpl @Inject constructor(
    private val movieDetailsRest: MovieDetailsRest,
    @Named("movie_details") private val movieDao: MovieDao,
) : MovieDetailsRepository {

    override suspend fun getMovie(movieId: Int): MovieDetailItem = MovieDetailItem(
        id = movieId,
        reviewList = getMovieReviews(movieId),
        previewList = getMoviePreviews(movieId)
    )

    override suspend fun getMovieReviews(movieId: Int): List<MovieReviewItem> =
        doApiCall { movieDetailsRest.getMovieReviews(movieId) }

    override suspend fun getMoviePreviews(movieId: Int): List<MoviePreviewItem> =
        doApiCall { movieDetailsRest.getMoviePreviews(movieId) }

    override suspend fun isFavorite(movieSchema: MovieDetailItem): Boolean =
        movieDao.findFavorite(movieSchema.id) != null

    override suspend fun addToFavorites(movieSchema: MovieDetailItem) {
//        movieDao.addFavorite(movieSchema)
        TODO("FIX ME")
    }

    override suspend fun removeFromFavorites(movie: MovieDetailItem) {
//        movieDao.removeFavorite(movie)
        TODO("FIX ME")
    }

    private suspend fun <R : Any, T> doApiCall(serviceApiCall: suspend () -> Response<R>): List<T> {
        val result = mutableListOf<T>()
        apiCall { serviceApiCall() }
            .onSuccess { response ->

            }
            .onFailure {
                // Womp womp
            }
        return result
    }
}