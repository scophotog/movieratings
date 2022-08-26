package org.sco.movieratings.usecase

import org.sco.movieratings.api.TheMovieDBService
import org.sco.movieratings.api.response.MoviePreview
import javax.inject.Inject

class GetMoviePreviews @Inject constructor(
    private val service: TheMovieDBService,
){

    suspend operator fun invoke(movieId: Int): List<MoviePreview> {
        return service.getMoviePreviews(movieId).getOrNull()?.moviePreviews ?: emptyList()
    }
}