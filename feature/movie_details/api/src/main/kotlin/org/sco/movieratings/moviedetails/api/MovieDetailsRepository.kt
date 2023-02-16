package org.sco.movieratings.moviedetails.api

interface MovieDetailsRepository {
    suspend fun getMovie(movieId: Int): MovieDetailItem
    suspend fun getMovieReviews(movieId: Int): List<MovieReviewItem>
    suspend fun getMoviePreviews(movieId: Int): List<MoviePreviewItem>

    suspend fun isFavorite(movieSchema: MovieDetailItem): Boolean
    suspend fun addToFavorites(movieSchema: MovieDetailItem)
    suspend fun removeFromFavorites(movie: MovieDetailItem)
}