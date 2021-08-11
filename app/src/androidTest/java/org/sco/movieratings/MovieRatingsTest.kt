package org.sco.movieratings

import android.widget.TextView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MovieRatingsTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun viewTopRated() {
        onView(withId(R.id.bn_top_rated))
            .check(matches(isDisplayed()))
        onView(withId(R.id.bn_top_rated)).perform(click())
        onView(
            allOf(
                instanceOf(
                    TextView::class.java
                ),
                withParent(withId(R.id.toolbar))
            )
        )
            .check(matches(withText("Top Rated")))
    }

    @Test
    fun viewMostPopular() {
        onView(withId(R.id.bn_most_popular))
            .check(matches(isDisplayed()))
        onView(withId(R.id.bn_most_popular)).perform(click())
        onView(
            allOf(
                instanceOf(
                    TextView::class.java
                ),
                withParent(withId(R.id.toolbar))
            )
        )
            .check(matches(withText("Most Popular")))
    }

    @Test
    fun viewFavorites() {
        onView(withId(R.id.bn_my_favorites))
            .check(matches(isDisplayed()))
        onView(withId(R.id.bn_my_favorites)).perform(click())
        onView(
            allOf(
                instanceOf(
                    TextView::class.java
                ),
                withParent(withId(R.id.toolbar))
            )
        )
            .check(matches(withText("Favorites")))
    }

    @Test
    fun addFavorite() {
        onView(withId(R.id.bn_top_rated)).perform(click())
        onView(withId(R.id.movieList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.mark_as_favorite)).perform(click())
        pressBack()
        onView(withId(R.id.bn_my_favorites)).perform(click())
        onView(
            allOf(
                instanceOf(
                    TextView::class.java
                ),
                withParent(withId(R.id.toolbar))
            )
        )
            .check(matches(withText("Favorites")))
        onView(withId(R.id.moviePoster))
            .check(matches(isDisplayed()))
        onView(withId(R.id.movieList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.mark_as_favorite)).perform(click())
        pressBack()
        onView(withId(R.id.bn_my_favorites)).perform(click())
        onView(
            allOf(
                instanceOf(
                    TextView::class.java
                ),
                withParent(withId(R.id.toolbar))
            )
        )
            .check(matches(withText("Favorites")))
        onView(withId(R.id.emptyView))
            .check(matches(withText("No movies :(")))
    }
}