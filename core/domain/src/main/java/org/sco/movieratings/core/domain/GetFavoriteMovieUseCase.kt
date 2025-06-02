package org.sco.movieratings.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.sco.movieratings.core.data.repository.MovieListRepository
import org.sco.movieratings.core.model.data.MovieListItem
import javax.inject.Inject

class GetFavoriteMovieUseCase @Inject constructor(
    private val repository: MovieListRepository,
) {

    suspend operator fun invoke(movieId: Int): Flow<MovieListItem?> =
        repository.getFavoriteMovies().map { movies ->
            movies.firstOrNull { it.id == movieId }
        }

}