package org.sco.espresso.robots

import org.sco.movieratings.R


class MovieDetailsRobot {

}

fun movieDetails(f: MovieDetailsRobot.() -> Unit) = MovieDetailsRobot().apply { f() }