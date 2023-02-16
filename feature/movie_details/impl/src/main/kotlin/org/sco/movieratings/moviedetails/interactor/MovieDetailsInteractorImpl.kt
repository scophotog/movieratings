package org.sco.movieratings.moviedetails.interactor

import org.sco.movieratings.moviedetails.api.MovieDetailInteractor
import org.sco.movieratings.moviedetails.api.MovieDetailItem
import org.sco.movieratings.moviedetails.usecase.GetMovieUseCase
import javax.inject.Inject

class MovieDetailsInteractorImpl @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase
): MovieDetailInteractor {
    override suspend fun getMovie(movieId: Int): MovieDetailItem =
        getMovieUseCase.invoke(movieId)
}