package org.sco.movieratings.robots

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AllOf
import org.sco.movieratings.R

class MovieListRobot {

    fun tapMostPopular() {
        checkMostPopularIsDisplayed()
        Espresso.onView(ViewMatchers.withId(R.id.bn_most_popular)).perform(ViewActions.click())
    }

    fun tapTopRated() {
        checkTopRatedIsDisplayed()
        Espresso.onView(ViewMatchers.withId(R.id.bn_top_rated)).perform(ViewActions.click())
    }

    fun tapFavorites() {
        checkFavoritesIsDisplayed()
        Espresso.onView(ViewMatchers.withId(R.id.bn_my_favorites)).perform(ViewActions.click())
    }

    fun tapMovieInPosition(position: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.movieList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                ViewActions.click()
            )
        )
    }

    fun checkEmptyListDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.emptyView))
            .check(ViewAssertions.matches(ViewMatchers.withText("No movies :(")))
    }

    fun checkMovieIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.moviePoster))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun checkTitleMatches(title: String) {
        Espresso.onView(
            AllOf.allOf(
                CoreMatchers.instanceOf(
                    TextView::class.java
                ),
                ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.withText(title)))
    }

    fun checkMovieWithTitleExists(movieTitle: String) {
        Espresso.onView(ViewMatchers.withId(R.id.movieTitle))
            .check(ViewAssertions.matches(ViewMatchers.withText(movieTitle)))

    }

    fun checkFavoritesIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.bn_my_favorites))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun checkTopRatedIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.bn_top_rated))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun checkMostPopularIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.bn_most_popular))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}

fun movieList(f: MovieListRobot.() -> Unit) = MovieListRobot().apply { f() }