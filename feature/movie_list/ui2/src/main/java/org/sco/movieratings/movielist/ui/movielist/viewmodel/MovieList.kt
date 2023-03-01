package org.sco.movieratings.movielist.ui.movielist.viewmodel

import org.sco.movieratings.shared.api.MovieListItem

data class MovieList(
    val title: String,
    val movieList: List<MovieListItem>
)
