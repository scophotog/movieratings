package org.sco.e2etests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.sco.movieratings.MainActivity

@RunWith(AndroidJUnit4::class)
class MovieRatingsTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun viewTopRated() {
    }

    @Test
    fun viewMostPopular() {
    }

    @Test
    fun viewFavorites() {
    }

    @Test
    fun addAndRemoveFavorite() {
    }
}