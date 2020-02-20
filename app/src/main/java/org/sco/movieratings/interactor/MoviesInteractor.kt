package org.sco.movieratings.interactor

import android.content.Context
import android.database.Cursor
import android.util.Log
import android.view.View
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.sco.movieratings.R
import org.sco.movieratings.activity.MainActivity
import org.sco.movieratings.api.ApiManager
import org.sco.movieratings.api.TheMovieDBService
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.db.MovieContract
import org.sco.movieratings.utility.MovieListRouter
import java.util.*
import java.util.concurrent.Callable

private const val TAG = "MoviesInteractor"
private const val RETRY_ON_ERROR_COUNT = 3L
class MoviesInteractor(private val movieListRouter: MovieListRouter) {

    val apiManager = ApiManager()

    fun getMovies(movieList: String): Observable<List<Movie>> {
        return apiManager.getService(TheMovieDBService::class.java).getMovies(movieList)
            .retry(RETRY_ON_ERROR_COUNT).flatMap {response ->
                if (response.movies.isNotEmpty()) {
                    Observable.just(response.movies)
                } else {
                    Observable.error(IllegalArgumentException("Failed to get list of movies"))
                }
            }
    }

    fun getFavorites(context: Context): Observable<List<Movie>> {
        return makeObservable(getFavoriteMovies(context))
            .subscribeOn(Schedulers.computation())
    }

    fun onMovieClicked(movie: Movie, context: Context) {
        if ((context as MainActivity).findViewById<View?>(R.id.movie_detail_container) != null) {
            movieListRouter.startFragment(movie)
        } else {
            movieListRouter.startActivity(movie, context)
        }
    }

    private fun getFavoriteMovies(context: Context): Callable<List<Movie>> {
        return Callable {
            val out: MutableList<Movie> =
                ArrayList()
            val c = context.contentResolver.query(
                MovieContract.CONTENT_URI,
                null,
                MovieContract.MovieEntry.IS_FAVORITE + " = 1",
                null,
                null,
                null
            )
            if (c != null && c.moveToFirst()) {
                do {
                    out.add(movieFromCursor(c))
                } while (c.moveToNext())
            } else {
                Log.e(TAG, "Cursor: Null")
            }
            out
        }
    }

    private fun <T> makeObservable(func: Callable<T>): Observable<T> {
        return Observable.create {
            try {
                it.onNext(func.call())
            }  catch (e: Exception) {
                Log.e(TAG, "Error reading from DB")
                e.printStackTrace()
            }
        }
    }

    companion object {
        fun movieFromCursor(c: Cursor): Movie {
            return Movie(c.getString(c.getColumnIndex(MovieContract.MovieEntry.MOVIE_TITLE)),
                c.getInt(c.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID)),
                c.getString(c.getColumnIndex(MovieContract.MovieEntry.POSTER_PATH)),
                c.getString(c.getColumnIndex(MovieContract.MovieEntry.OVERVIEW)),
                c.getString(c.getColumnIndex(MovieContract.MovieEntry.RELEASE_DATE)),
                c.getDouble(c.getColumnIndex(MovieContract.MovieEntry.POPULARITY)),
                c.getDouble(c.getColumnIndex(MovieContract.MovieEntry.RATING)))
        }
    }

}