package org.sco.movieratings.mocked

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.sco.movieratings.BaseFragmentTest
import org.sco.movieratings.api.ApiKeyModule
import org.sco.movieratings.api.BaseUrlModule
import org.sco.movieratings.api.ImagePathUrlModule
import org.sco.movieratings.robots.movieList

@HiltAndroidTest
@UninstallModules(BaseUrlModule::class, ApiKeyModule::class, ImagePathUrlModule::class)
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