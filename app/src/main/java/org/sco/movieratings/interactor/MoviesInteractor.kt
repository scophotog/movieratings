package org.sco.movieratings.interactor

import android.content.Context
import android.view.View
import org.sco.movieratings.R
import org.sco.movieratings.activity.MainActivity
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.utility.MovieListRouter

private const val TAG = "MoviesInteractor"
class MoviesInteractor(private val movieListRouter: MovieListRouter) {

    fun onMovieClicked(movie: Movie, context: Context) {
        if ((context as MainActivity).findViewById<View?>(R.id.movie_detail_container) != null) {
            movieListRouter.startFragment(movie)
        } else {
            movieListRouter.startActivity(movie, context)
        }
    }
}