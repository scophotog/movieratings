package org.sco.movieratings.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.sco.espresso.robots.movieList
import org.sco.movieratings.BaseFragmentTest

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PopularTest: BaseFragmentTest() {

    @Before
    fun popularTestSetup() {
        launchMovieListFragment()
    }

    @Test
    fun viewPopularMovies_seeExpectedMovieInList() {
        movieList {
            tapMostPopular()
            checkTitleMatches("Most Popular")
            checkMovieWithTitleExists("Popular Movie 2")
        }
    }

}