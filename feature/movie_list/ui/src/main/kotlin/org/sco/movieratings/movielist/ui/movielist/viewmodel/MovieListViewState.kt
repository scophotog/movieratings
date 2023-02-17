package org.sco.movieratings.movielist.ui.movielist.viewmodel

import org.sco.movieratings.movielist.api.MovieListItem

sealed class MovieListViewState {
    object Loading : MovieListViewState()
    data class Loaded(val moveList: List<MovieListItem>) : MovieListViewState()
    object Empty: MovieListViewState()
}