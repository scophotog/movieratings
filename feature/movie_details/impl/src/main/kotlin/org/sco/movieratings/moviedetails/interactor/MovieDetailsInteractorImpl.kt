package org.sco.movieratings.moviedetails.interactor

import org.sco.movieratings.moviedetails.api.MovieDetailInteractor
import org.sco.movieratings.moviedetails.usecase.GetMovieUseCase
import org.sco.movieratings.shared.api.MovieListItem
import javax.inject.Inject

class MovieDetailsInteractorImpl @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase
): MovieDetailInteractor {
    override suspend fun getMovie(movieId: Int): MovieListItem =
        getMovieUseCase.invoke(movieId)
}