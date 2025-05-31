package org.sco.movieratings.movielist.ui.movielist.viewmodel

sealed class MovieListViewState {
    data object Loading : MovieListViewState()
    data class Loaded(val movieListState: MovieListState) : MovieListViewState()
    data object Empty: MovieListViewState()
}