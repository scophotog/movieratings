package org.sco.movieratings.usecase

import org.sco.movieratings.api.TheMovieDBService
import org.sco.movieratings.api.response.Review
import java.io.IOException
import javax.inject.Inject

class GetMovieReviews @Inject constructor(
    private val service: TheMovieDBService,
){

    suspend operator fun invoke(movieId: Int): Result<List<Review>> {
        val reviews = service.getMovieReviews(movieId)
        return if (reviews.isSuccessful) {
            reviews.body()?.reviews?.let {
                Result.success(it)
            } ?: Result.failure(IllegalStateException("Empty Response"))
        } else {
            Result.failure(IOException("Bad Response: ${reviews.errorBody()}"))
        }
    }

}