package org.sco.movieratings.movielist.usecase

import org.sco.movieratings.movielist.api.MovieListItem
import org.sco.movieratings.movielist.api.MovieListRepository
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val repository: MovieListRepository
){

    suspend operator fun invoke(): List<MovieListItem> =
        repository.getFavoriteMovies()
}