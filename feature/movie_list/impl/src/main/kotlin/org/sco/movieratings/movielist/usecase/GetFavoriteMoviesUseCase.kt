package org.sco.movieratings.movielist.usecase

import org.sco.movieratings.shared.api.MovieListItem
import org.sco.movieratings.shared.api.MovieListRepository
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val repository: MovieListRepository
){

    suspend operator fun invoke(): List<MovieListItem> =
        repository.getFavoriteMovies()
}