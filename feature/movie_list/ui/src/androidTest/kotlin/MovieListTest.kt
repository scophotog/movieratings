import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.sco.movieratings.movielist.ui.movielist.compose.MovieList
import org.sco.movieratings.movielist.ui.movielist.viewmodel.MovieListViewState
import org.sco.movieratings.shared.api.MovieListItem

class MovieListTest {

    @get:Rule(order = 2)
    var composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            MovieList(
                modifier = Modifier,
                viewState = MovieListViewState.Loaded(listOf(movie)),
                onMovieClick = { }
            )
        }
    }

    @Test
    fun app_displays_list_of_items() {
        composeTestRule.onNodeWithTag("MovieList").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription(movie.title).assertExists()
    }

    companion object {
        private val movie = MovieListItem(
            id = 0,
            title = "Something",
            posterPath = ""
        )
    }
}