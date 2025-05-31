package org.sco.movieratings.movielist.ui.movielist.viewmodel

sealed class MovieListViewState {
    data object Loading : MovieListViewState()
    data class Loaded(val movieList: List<MovieList>) : MovieListViewState()
    data object Empty: MovieListViewState()
}