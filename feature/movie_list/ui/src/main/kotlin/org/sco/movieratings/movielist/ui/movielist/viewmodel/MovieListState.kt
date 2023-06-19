package org.sco.movieratings.movielist.ui.movielist.viewmodel

import org.sco.movieratings.movielist.api.MovieListType
import org.sco.movieratings.shared.api.MovieListItem

data class MovieListState(
    val movieList: List<MovieListItem>,
    val type: MovieListType
)
