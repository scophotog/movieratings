package org.sco.movieratings.movielist.ui.movielist.viewmodel

sealed class MovieListViewState {
    object Loading : MovieListViewState()
    data class Loaded(val movieListState: MovieListState) : MovieListViewState()
    object Empty: MovieListViewState()
}