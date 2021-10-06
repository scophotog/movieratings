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
class TopRatedTest: BaseFragmentTest() {

    @Before
    fun popularTestSetup() {
        launchMovieListFragment()
    }

    @Test
    fun viewTopRatedMovies_seeExpectedMovieInList() {
        movieList {
            tapTopRated()
            checkTitleMatches("Top Rated")
            checkMovieWithTitleExists("Top Rated Movie")
        }
    }

}