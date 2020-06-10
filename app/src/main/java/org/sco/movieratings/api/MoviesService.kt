package org.sco.movieratings.api

import android.content.Context
import io.reactivex.Observable
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.api.response.MoviesResponse
import org.sco.movieratings.api.response.PreviewsResponse
import org.sco.movieratings.api.response.ReviewsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesService {

    @Inject
    lateinit var api: TheMovieDBService

    @Inject
    lateinit var db: DBService

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getMovieReviews(id: Int): Observable<ReviewsResponse> {
        return api.getMovieReviews(id)
    }

    fun getMoviePreviews(id: Int): Observable<PreviewsResponse> {
        return api.getMoviePreviews(id)
    }

    fun getTopRatedMovies(): Observable<MoviesResponse> {
        return api.getTopRatedMovies()
    }

    fun getPopularMovies(): Observable<MoviesResponse> {
        return api.getPopularMovies()
    }

    fun getFavoriteMovies(context: Context): Observable<List<Movie>> {
        return db.getFavoriteMovies(context)
    }
}