package org.sco.movieratings.core.domain

import kotlinx.coroutines.flow.Flow
import org.sco.movieratings.core.data.repository.MovieListRepository
import org.sco.movieratings.core.model.data.MovieListItem
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val repository: MovieListRepository
){

    suspend operator fun invoke(): Flow<List<MovieListItem>> =
        repository.getFavoriteMovies()
}