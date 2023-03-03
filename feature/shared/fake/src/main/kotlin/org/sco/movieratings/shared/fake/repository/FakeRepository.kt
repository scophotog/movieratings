package org.sco.movieratings.shared.fake.repository

import org.sco.movieratings.shared.api.MovieListItem
import org.sco.movieratings.shared.api.MovieListRepository
import org.sco.movieratings.shared.api.MoviePreviewItem
import org.sco.movieratings.shared.api.MovieReviewItem

class FakeRepository : MovieListRepository {
    override suspend fun getPopularMovies(refresh: Boolean): List<MovieListItem> {
        val movie = getMovie(0)
        return listOf(movie, movie, movie)
    }

    override suspend fun getTopRatedMovies(refresh: Boolean): List<MovieListItem> {
        val movie = getMovie(0)
        return listOf(movie, movie, movie)
    }

    override suspend fun getFavoriteMovies(): List<MovieListItem> {
        val movie = getMovie(0)
        return listOf(movie, movie, movie)
    }

    override suspend fun getMovie(movieId: Int): MovieListItem {
        return when(movieId) {
            MOVIE_WITH_NO_PREVIEWS -> {
                MovieListItem(
                    id = MOVIE_WITH_NO_PREVIEWS,
                    title = "Fake",
                    posterPath = null,
                    overview = "Fake movie",
                    previewList = mutableListOf(),
                    reviewList = getMovieReviews(0).toMutableList()
                )
            }
            else -> {
                MovieListItem(
                    id = MOVIE_WITH_PREVIEWS,
                    title = "Fake",
                    posterPath = null,
                    overview = "Fake movie",
                    previewList = getMoviePreviews(0).toMutableList(),
                    reviewList = getMovieReviews(0).toMutableList()
                )
            }
        }
    }


    override suspend fun getMovieReviews(movieId: Int): List<MovieReviewItem> {
        val reviewItem = MovieReviewItem(
            id = "0",
            author = "Test Author",
            content = "Test content",
            url = "http://url"
        )
        return listOf(reviewItem, reviewItem, reviewItem)
    }

    override suspend fun getMoviePreviews(movieId: Int): List<MoviePreviewItem> {
        val previewItem = MoviePreviewItem()
        return listOf(previewItem, previewItem, previewItem)
    }

    override suspend fun isFavorite(movieId: Int): Boolean {
        return true
    }

    override suspend fun addToFavorites(movieId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        const val MOVIE_WITH_PREVIEWS = 0
        const val MOVIE_WITH_NO_PREVIEWS = 1
    }
}