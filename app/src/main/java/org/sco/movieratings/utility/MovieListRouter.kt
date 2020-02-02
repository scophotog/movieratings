package org.sco.movieratings.utility

import android.content.Context
import androidx.fragment.app.FragmentManager
import org.sco.movieratings.R
import org.sco.movieratings.activity.MovieActivity.Companion.newInstance
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.fragment.MovieFragment

class MovieListRouter(fragmentManager: FragmentManager) {

    private val navigationManager: NavigationManager = NavigationManager(fragmentManager)

    fun startFragment(movie: Movie) {
        val movieFragment = MovieFragment.newInstance(movie)
        navigationManager.navigateTo(movieFragment, R.id.movie_detail_container)
    }

    fun startActivity(
        movie: Movie,
        context: Context
    ) {
        val movieActivity = newInstance(movie, context)
        navigationManager.navigateToActivity(movieActivity, context)
    }

}