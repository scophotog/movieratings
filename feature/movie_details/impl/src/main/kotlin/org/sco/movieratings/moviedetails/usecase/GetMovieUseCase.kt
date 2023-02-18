package org.sco.movieratings.moviedetails.usecase

import org.sco.movieratings.shared.api.MovieListItem
import org.sco.movieratings.shared.api.MovieListRepository
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(
    private val repository: MovieListRepository,
) {

    suspend operator fun invoke(movieId: Int): MovieListItem =
        repository.getMovie(movieId) ?: throw IllegalArgumentException("Movie not found")

}