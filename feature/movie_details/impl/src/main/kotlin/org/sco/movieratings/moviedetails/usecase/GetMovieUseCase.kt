package org.sco.movieratings.moviedetails.usecase

import org.sco.movieratings.moviedetails.api.MovieDetailItem
import org.sco.movieratings.moviedetails.api.MovieDetailsRepository
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(
    private val repository: MovieDetailsRepository,
) {

    suspend operator fun invoke(movieId: Int): MovieDetailItem =
        repository.getMovie(movieId)
}