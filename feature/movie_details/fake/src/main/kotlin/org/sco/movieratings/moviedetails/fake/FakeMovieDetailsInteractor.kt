package org.sco.movieratings.moviedetails.fake

import org.sco.movieratings.moviedetails.api.MovieDetailInteractor
import org.sco.movieratings.moviedetails.api.MovieDetailItem

class FakeMovieDetailsInteractor: MovieDetailInteractor {

    // TODO add more details
    override suspend fun getMovie(movieId: Int): MovieDetailItem =
        MovieDetailItem(
            id = 1
        )
}