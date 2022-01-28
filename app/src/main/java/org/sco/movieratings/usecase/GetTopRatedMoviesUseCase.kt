package org.sco.movieratings.usecase

import org.sco.movieratings.api.TheMovieDBService
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.repository.MovieMapper
import javax.inject.Inject

class GetTopRatedMoviesUseCase @Inject constructor(
    private val service: TheMovieDBService,
    private val mapper: MovieMapper
){

    suspend operator fun invoke(): List<MovieSchema> {
        return service.getTopRatedMovies().getOrNull()?.movies?.let {
            mapper(it)
        } ?: emptyList()
    }
}