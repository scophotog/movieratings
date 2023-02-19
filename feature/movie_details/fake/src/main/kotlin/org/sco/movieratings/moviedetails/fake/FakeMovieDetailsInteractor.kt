package org.sco.movieratings.moviedetails.fake

import org.sco.movieratings.moviedetails.api.MovieDetailInteractor
import org.sco.movieratings.shared.api.MovieListItem

class FakeMovieDetailsInteractor: MovieDetailInteractor {

    private var isFavorite = false
    override suspend fun getMovie(movieId: Int): MovieListItem =
        MovieListItem(
            id = 1,
            title = "Fake",
            posterPath = ""
        )

    override suspend fun isFavorite(movieId: Int) = isFavorite

    override suspend fun addFavorite(movieId: Int) {
        isFavorite = true
    }

    override suspend fun removeFavorite(movieId: Int) {
        isFavorite = false
    }
}