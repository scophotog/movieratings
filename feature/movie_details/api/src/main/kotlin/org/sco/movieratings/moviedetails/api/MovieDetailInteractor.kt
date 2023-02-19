package org.sco.movieratings.moviedetails.api

import org.sco.movieratings.shared.api.MovieListItem

interface MovieDetailInteractor {
    suspend fun getMovie(movieId: Int): MovieListItem

    suspend fun isFavorite(movieId: Int): Boolean

    suspend fun addFavorite(movieId: Int)

    suspend fun removeFavorite(movieId: Int)
}