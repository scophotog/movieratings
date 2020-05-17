package org.sco.movieratings.api

import android.content.Context
import io.reactivex.Single
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.api.response.PreviewsResponse
import org.sco.movieratings.api.response.ReviewsResponse
import org.sco.movieratings.fragment.MovieType
import javax.inject.Inject

class MoviesService {

    @Inject
    lateinit var api: TheMovieDBService

    @Inject
    lateinit var db: DBService

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getMovies(movieListType: MovieType, context: Context?): Single<List<Movie>> {
        return when(movieListType) {
            MovieType.POPULAR -> getPopularMovies()
            MovieType.TOP_RATED -> getTopRatedMovies()
            MovieType.FAVORITE -> getFavoriteMovies(context!!)
        }
    }

    fun getMovieReviews(id: Int): Single<ReviewsResponse> {
        return api.getMovieReviews(id)
    }

    fun getMoviePreviews(id: Int): Single<PreviewsResponse> {
        return api.getMoviePreviews(id)
    }

    private fun getTopRatedMovies(): Single<List<Movie>> {
        return api.getTopRatedMovies().map { it.movies }
    }

    private fun getPopularMovies(): Single<List<Movie>> {
        return api.getPopularMovies().map { it.movies }
    }

    private fun getFavoriteMovies(context: Context): Single<List<Movie>> {
        return db.getFavoriteMovies(context)
    }
}