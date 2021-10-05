package org.sco.movieratings.e2e

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.sco.movieratings.MainActivity
import org.sco.movieratings.robots.movieDetails
import org.sco.movieratings.robots.movieList

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class MovieRatingsTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun viewTopRated() {
        movieList {
            tapMostPopular()
            checkTitleMatches("Top Rated")
        }
    }

    @Test
    fun viewMostPopular() {
        movieList {
            tapMostPopular()
            checkTitleMatches("Most Popular")
        }
    }

    @Test
    fun viewFavorites() {
        movieList {
            tapFavorites()
            checkTitleMatches("Favorites")
        }
    }

    @Test
    fun addAndRemoveFavorite() {
        movieList {
            tapTopRated()
            tapMovieInPosition(0)
        }

        movieDetails {
            markAsFavorite()
            pressBack()
        }

        movieList {
            tapFavorites()
            checkTitleMatches("Favorites")
            checkMovieIsDisplayed()
            tapMovieInPosition(0)
        }

        movieDetails {
            unMarkAsFavorite()
            pressBack()
        }

        movieList {
            tapFavorites()
            checkTitleMatches("Favorites")
            checkMovieIsDisplayed()
            tapMovieInPosition(0)
            checkEmptyListDisplayed()
        }
    }
}