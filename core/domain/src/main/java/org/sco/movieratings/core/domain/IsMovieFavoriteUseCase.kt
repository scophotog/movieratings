package org.sco.movieratings.core.domain

import kotlinx.coroutines.flow.Flow
import org.sco.movieratings.core.data.repository.MovieListRepository
import javax.inject.Inject

class IsMovieFavoriteUseCase @Inject constructor(
    private val repository: MovieListRepository,
) {

    suspend operator fun invoke(movieId: Int): Flow<Boolean> =
        repository.isFavorite(movieId)

}