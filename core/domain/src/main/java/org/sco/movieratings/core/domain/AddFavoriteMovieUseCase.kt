package org.sco.movieratings.core.domain

import org.sco.movieratings.core.data.repository.MovieListRepository
import javax.inject.Inject

class AddFavoriteMovieUseCase @Inject constructor(
    private val repository: MovieListRepository,
) {

    suspend operator fun invoke(movieId: Int) {
        repository.addToFavorites(movieId)
    }

}