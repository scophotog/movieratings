package org.sco.movieratings.moviedetails.api

import org.sco.movieratings.shared.api.MovieListItem

interface MovieDetailInteractor {
    suspend fun getMovie(movieId: Int): MovieListItem
}