package org.sco.movieratings.movielist.api

interface MovieListRepository {
    suspend fun getPopularMovies(refresh: Boolean = false): List<MovieListItem>
    suspend fun getTopRatedMovies(refresh: Boolean = false): List<MovieListItem>
    suspend fun getFavoriteMovies(): List<MovieListItem>
}