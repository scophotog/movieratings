package org.sco.espresso.robots


class MovieDetailsRobot {

}

fun movieDetails(f: MovieDetailsRobot.() -> Unit) = MovieDetailsRobot().apply { f() }