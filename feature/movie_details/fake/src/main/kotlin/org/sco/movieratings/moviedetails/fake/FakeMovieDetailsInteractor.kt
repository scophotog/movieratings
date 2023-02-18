package org.sco.movieratings.moviedetails.fake

import org.sco.movieratings.moviedetails.api.MovieDetailInteractor
import org.sco.movieratings.shared.api.MovieListItem

class FakeMovieDetailsInteractor: MovieDetailInteractor {

    override suspend fun getMovie(movieId: Int): MovieListItem =
        MovieListItem(
            id = 1,
            title = "Fake",
            posterPath = ""
        )
}