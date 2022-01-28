package org.sco.movieratings.usecase

import org.sco.movieratings.api.TheMovieDBService
import org.sco.movieratings.api.response.Review
import javax.inject.Inject

class GetMovieReviews @Inject constructor(
    private val service: TheMovieDBService,
){

    suspend operator fun invoke(movieId: Int): List<Review> {
        return service.getMovieReviews(movieId).getOrNull()?.reviews ?: emptyList()
    }
}