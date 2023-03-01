package org.sco.movieratings.movielist.ui.movielist.viewmodel

sealed class MovieListViewState {
    object Loading : MovieListViewState()
    data class Loaded(val movieList: List<MovieList>) : MovieListViewState()
    object Empty: MovieListViewState()
}