package org.sco.movieratings.robots

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.sco.movieratings.R


class MovieDetailsRobot {

    fun markAsFavorite() {
        Espresso.onView(ViewMatchers.withId(R.id.mark_as_favorite)).perform(ViewActions.click())
    }

    fun unMarkAsFavorite() {
        Espresso.onView(ViewMatchers.withId(R.id.mark_as_favorite)).perform(ViewActions.click())

    }

}

fun movieDetails(f: MovieDetailsRobot.() -> Unit) = MovieDetailsRobot().apply { f() }