package org.sco.espresso.robots

import org.sco.movieratings.R


class MovieDetailsRobot {

    fun markAsFavorite() {
        clickElementWithId(R.id.favorite_fab)
    }

    fun unMarkAsFavorite() {
        clickElementWithId(R.id.favorite_fab)
    }

}

fun movieDetails(f: MovieDetailsRobot.() -> Unit) = MovieDetailsRobot().apply { f() }