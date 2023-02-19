package org.sco.movieratings.moviedetails.usecase

import org.sco.movieratings.shared.api.MovieListRepository
import javax.inject.Inject

class RemoveFavoriteMovieUseCase @Inject constructor(
    private val repository: MovieListRepository,
) {

    suspend operator fun invoke(movieId: Int) {
        repository.removeFromFavorites(movieId)
    }

}