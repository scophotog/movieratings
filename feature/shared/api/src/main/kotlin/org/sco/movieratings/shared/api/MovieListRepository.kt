package org.sco.movieratings.shared.api

interface MovieListRepository {
    suspend fun getPopularMovies(refresh: Boolean = false): List<MovieListItem>
    suspend fun getTopRatedMovies(refresh: Boolean = false): List<MovieListItem>
    suspend fun getFavoriteMovies(): List<MovieListItem>

    suspend fun getMovie(movieId: Int): MovieListItem?
    suspend fun getMovieReviews(movieId: Int): List<MovieReviewItem>
    suspend fun getMoviePreviews(movieId: Int): List<MoviePreviewItem>

    suspend fun isFavorite(movieSchema: MovieListItem): Boolean
    suspend fun addToFavorites(movieSchema: MovieListItem)
    suspend fun removeFromFavorites(movie: MovieListItem)
}