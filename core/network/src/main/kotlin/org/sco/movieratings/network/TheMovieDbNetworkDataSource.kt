package org.sco.movieratings.network

import org.sco.movieratings.network.model.Movie
import org.sco.movieratings.network.model.MoviePreview
import org.sco.movieratings.network.model.Review

interface TheMovieDbNetworkDataSource {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getTopRatedMovies(): List<Movie>
    suspend fun getMovieReviews(movieId: Int): List<Review>
    suspend fun getMoviePreviews(movieId: Int): List<MoviePreview>
}