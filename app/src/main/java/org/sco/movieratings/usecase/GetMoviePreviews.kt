package org.sco.movieratings.usecase

import org.sco.movieratings.api.TheMovieDBService
import org.sco.movieratings.api.response.Preview
import java.io.IOException
import javax.inject.Inject

class GetMoviePreviews @Inject constructor(
    private val service: TheMovieDBService,
){

    suspend operator fun invoke(movieId: Int): Result<List<Preview>> {
        val previews = service.getMoviePreviews(movieId)
        return if (previews.isSuccessful) {
            previews.body()?.previews?.let {
                Result.success(it)
            } ?: Result.failure(IllegalStateException("Empty Response"))
        } else {
            Result.failure(IOException("Bad Response: ${previews.errorBody()}"))
        }
    }

}