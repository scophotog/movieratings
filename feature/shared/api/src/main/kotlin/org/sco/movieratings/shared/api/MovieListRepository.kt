package org.sco.movieratings.shared.api

interface MovieListRepository {
    suspend fun getPopularMovies(refresh: Boolean = false): List<MovieListItem>
    suspend fun getTopRatedMovies(refresh: Boolean = false): List<MovieListItem>
    suspend fun getFavoriteMovies(): List<MovieListItem>

    suspend fun getMovie(movieId: Int): MovieListItem?
    suspend fun getMovieReviews(movieId: Int): List<MovieReviewItem>
    suspend fun getMoviePreviews(movieId: Int): List<MoviePreviewItem>

    suspend fun isFavorite(movieId: Int): Boolean
    suspend fun addToFavorites(movieId: Int)
    suspend fun removeFromFavorites(movieId: Int)
}