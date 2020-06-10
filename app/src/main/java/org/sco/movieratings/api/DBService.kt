package org.sco.movieratings.api

import android.content.Context
import android.database.Cursor
import android.util.Log
import io.reactivex.Observable
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.db.MovieContract
import java.util.*

const val TAG = "DBService"
class DBService {

    fun getFavoriteMovies(context: Context): Observable<List<Movie>> {
        val out: MutableList<Movie> = ArrayList()
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
        return Observable.just(out)
    }

    private fun movieFromCursor(c: Cursor): Movie {
        return Movie(c.getString(c.getColumnIndex(MovieContract.MovieEntry.MOVIE_TITLE)),
            c.getInt(c.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID)),
            c.getString(c.getColumnIndex(MovieContract.MovieEntry.POSTER_PATH)),
            c.getString(c.getColumnIndex(MovieContract.MovieEntry.OVERVIEW)),
            c.getString(c.getColumnIndex(MovieContract.MovieEntry.RELEASE_DATE)),
            c.getDouble(c.getColumnIndex(MovieContract.MovieEntry.POPULARITY)),
            c.getDouble(c.getColumnIndex(MovieContract.MovieEntry.RATING)))
    }
}