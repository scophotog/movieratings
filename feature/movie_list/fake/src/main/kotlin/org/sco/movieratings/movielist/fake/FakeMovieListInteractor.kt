package org.sco.movieratings.movielist.fake

import org.sco.movieratings.movielist.api.MovieListInteractor
import org.sco.movieratings.movielist.api.MovieListItem

class FakeMovieListInteractor : MovieListInteractor {
    override suspend fun getPopularMovies(refresh: Boolean): List<MovieListItem> {
        val list = mutableListOf<MovieListItem>()
        repeat(5) {
            list.add(MovieListItem(
                id = it,
                title = "popular${it}",
                posterPath = ""
            ))
        }
        return list
    }

    override suspend fun getTopRatedMovies(refresh: Boolean): List<MovieListItem> {
        val list = mutableListOf<MovieListItem>()
        repeat(5) {
            list.add(MovieListItem(
                id = it,
                title = "top${it}",
                posterPath = ""
            ))
        }
        return list
    }

    override suspend fun getFavoriteMovies(): List<MovieListItem> {
        val list = mutableListOf<MovieListItem>()
        repeat(5) {
            list.add(MovieListItem(
                id = it,
                title = "favorite${it}",
                posterPath = ""
            ))
        }
        return list
    }
}