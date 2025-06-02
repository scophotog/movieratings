package org.sco.movieratings.movielist.ui.movielist.viewmodel

import org.sco.movieratings.core.model.data.MovieListItem

data class MovieListState(
    val movieList: List<MovieListItem>,
    val type: MovieListType
)
