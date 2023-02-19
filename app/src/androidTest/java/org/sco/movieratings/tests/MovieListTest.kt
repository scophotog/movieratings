package org.sco.movieratings.tests

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.sco.movieratings.MainActivity
import org.sco.movieratings.movielist.api.MovieListType
import org.sco.movieratings.movielist.ui.movielist.compose.MovieList
import org.sco.movieratings.movielist.ui.movielist.viewmodel.MovieListViewModel
import org.sco.movieratings.shared.api.MovieListItem

@HiltAndroidTest
class MovieListTest {

    @get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltTestRule.inject()
        composeTestRule.activity.setContent {
            MovieList(
                viewModel = composeTestRule.activity.viewModels<MovieListViewModel>().value,
                movieListType = MovieListType.POPULAR,
                onItemClick = { }
            )
        }
    }

    @Test
    fun app_displays_list_of_items() {
        composeTestRule.onNodeWithTag("MovieList").assertIsDisplayed()

        fakeMovies.forEach { item ->
            composeTestRule.onNodeWithContentDescription(item.title).assertExists()
        }
    }

    companion object {
        private val movie = MovieListItem(
            id = 0,
            title = "Something",
            posterPath = ""
        )
        val fakeMovies = listOf(movie,movie,movie)
    }
}