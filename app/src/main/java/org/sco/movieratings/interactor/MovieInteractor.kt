package org.sco.movieratings.interactor

import io.reactivex.Single
import org.sco.movieratings.api.ApiManager
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.api.models.Preview
import org.sco.movieratings.api.models.Review

private const val TAG = "MovieInteractor"
private const val RETRY_ON_ERROR_COUNT = 3L

class MovieInteractor {

    val apiManager = ApiManager()

    fun getReviews(movie: Movie): Single<List<Review>> {
        return apiManager.provideMoviesService().getMovieReviews(movie.id)
            .retry(RETRY_ON_ERROR_COUNT)
            .flatMap { response ->
                Single.just(response.reviews)
            }
    }

    fun getPreviews(movie: Movie): Single<List<Preview>> {
        return apiManager.provideMoviesService().getMoviePreviews(movie.id)
            .retry(RETRY_ON_ERROR_COUNT)
            .flatMap { response ->
                Single.just(response.previews)
            }
    }

}