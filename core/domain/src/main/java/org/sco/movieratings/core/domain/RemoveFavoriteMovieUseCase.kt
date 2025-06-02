package org.sco.movieratings.core.domain

import org.sco.movieratings.core.data.repository.MovieListRepository
import javax.inject.Inject

class RemoveFavoriteMovieUseCase @Inject constructor(
    private val repository: MovieListRepository,
) {

    suspend operator fun invoke(movieId: Int) {
        repository.removeFromFavorites(movieId)
    }

}