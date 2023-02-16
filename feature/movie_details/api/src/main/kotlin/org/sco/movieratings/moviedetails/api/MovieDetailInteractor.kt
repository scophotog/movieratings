package org.sco.movieratings.moviedetails.api

interface MovieDetailInteractor {
    suspend fun getMovie(movieId: Int): MovieDetailItem
}