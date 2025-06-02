import androidx.compose.ui.Modifier
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test
import org.sco.movieratings.movielist.ui.movielist.compose.MovieList
import org.sco.movieratings.movielist.ui.movielist.viewmodel.MovieListState
import org.sco.movieratings.movielist.ui.movielist.viewmodel.MovieListType
import org.sco.movieratings.movielist.ui.movielist.viewmodel.MovieListViewState
import org.sco.movieratings.shared.api.MovieListItem

class MovieListPaparazziTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
    )

    @Test
    fun launchCompose() {
        paparazzi.snapshot {
            MovieList(
                modifier = Modifier,
                viewState = MovieListViewState.Loaded(movieListState),
                onMovieClick = { }
            )
        }
    }

    companion object {

        private val movie = MovieListItem(
            id = 0,
            title = "Something",
            posterPath = ""
        )
        private val movieListState = MovieListState(
            movieList = listOf(movie, movie),
            type = MovieListType.POPULAR
        )
    }
}