package org.sco.movieratings.interactor

import io.reactivex.Observable
import org.sco.movieratings.api.ApiManager
import org.sco.movieratings.api.TheMovieDBService
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.api.models.Preview
import org.sco.movieratings.api.models.Review

private const val TAG = "MovieInteractor"
private const val RETRY_ON_ERROR_COUNT = 3L

class MovieInteractor {

    val apiManager = ApiManager()

    fun getReviews(movie: Movie): Observable<List<Review>> {
        return apiManager.getService(TheMovieDBService::class.java).getMovieReviews(movie.id)
            .retry(RETRY_ON_ERROR_COUNT)
            .flatMap { response ->
                Observable.just(response.reviews)
            }
    }

    fun getPreviews(movie: Movie): Observable<List<Preview>> {
        return apiManager.getService(TheMovieDBService::class.java).getMoviePreviews(movie.id)
            .retry(RETRY_ON_ERROR_COUNT)
            .flatMap { response ->
                Observable.just(response.previews)
            }
    }

}