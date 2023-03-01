package org.sco.movieratings.movielist.ui.movielist.viewmodel

import org.sco.movieratings.shared.api.MovieListItem

sealed class MovieListViewState {
    object Loading : MovieListViewState()
    data class Loaded(val movieList: List<MovieListItem>) : MovieListViewState()
    object Empty: MovieListViewState()
}