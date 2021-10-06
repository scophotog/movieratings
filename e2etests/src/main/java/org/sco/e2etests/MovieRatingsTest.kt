package org.sco.e2etests

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.sco.espresso.robots.movieDetails
import org.sco.espresso.robots.movieList
import org.sco.movieratings.MainActivity

@RunWith(AndroidJUnit4::class)
class MovieRatingsTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun viewTopRated() {
        movieList {
            tapTopRated()
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
            checkEmptyListDisplayed()
        }
    }
}