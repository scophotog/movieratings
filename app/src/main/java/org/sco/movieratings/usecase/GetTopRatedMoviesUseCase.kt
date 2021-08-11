package org.sco.movieratings.usecase

import org.sco.movieratings.api.TheMovieDBService
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.repository.MovieMapper
import java.io.IOException
import javax.inject.Inject

class GetTopRatedMoviesUseCase @Inject constructor(
    private val service: TheMovieDBService,
    private val mapper: MovieMapper
){

    suspend operator fun invoke(): Result<List<MovieSchema>> {
        val movieList = service.getTopRatedMovies()
        return if (movieList.isSuccessful) {
            movieList.body()?.movies?.let {
                Result.success(mapper(it))
            } ?: Result.failure(IllegalStateException("Empty Response"))
        } else {
            Result.failure(IOException("Bad Response: ${movieList.errorBody()}"))
        }
    }

}