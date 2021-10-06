package org.sco.espresso.robots

import org.sco.movieratings.R


class MovieDetailsRobot {

    fun markAsFavorite() {
        clickElementWithId(R.id.mark_as_favorite)
    }

    fun unMarkAsFavorite() {
        clickElementWithId(R.id.mark_as_favorite)
    }

}

fun movieDetails(f: MovieDetailsRobot.() -> Unit) = MovieDetailsRobot().apply { f() }