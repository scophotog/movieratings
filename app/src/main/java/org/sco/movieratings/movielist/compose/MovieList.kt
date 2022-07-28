package org.sco.movieratings.movielist.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import org.sco.movieratings.R
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.movielist.MovieListViewModel
import org.sco.movieratings.utility.Result


@Composable
fun MovieList(
    viewModel: MovieListViewModel = viewModel(),
    selectMovie: (Int) -> Unit = { }
) {
    Column {
        when (val state = viewModel.viewState.collectAsState().value) {
            is Result.Empty -> Text(text = "No movies")
            is Result.Error -> Text(text = "Oh noes")
            is Result.InProgress -> CircularProgressIndicator()
            is Result.Success -> MovieListLoaded(data = state.data, selectMovie = selectMovie)
        }
    }
}

@Composable
fun MovieListLoaded(data: List<MovieSchema>, selectMovie: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(data) { item ->
            MovieItem(item, selectMovie)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, heightDp = 180)
@Composable
fun MovieListPreview() {
    MaterialTheme {
        MovieListLoaded(
            data = movieData,
            selectMovie = { }
        )
    }
}

private val movieData = listOf(
    MovieSchema.mock(), MovieSchema.mock(), MovieSchema.mock(), MovieSchema.mock()
)

@Composable
fun MovieItem(
    movie: MovieSchema,
    selectMovie: (Int) -> Unit = { },
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clickable(
                onClick = { selectMovie(movie.id) }
            ),
        color = MaterialTheme.colors.onBackground,
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        NetworkImage(
            url = movie.posterPath,
            contentDescription = movie.title
        )
    }
}

@Composable
fun NetworkImage(
    url: String,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.FillWidth,
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewMovieItem() {
    MaterialTheme {
        MovieItem(MovieSchema.mock())
    }
}

@Preview
@Composable
fun PreviewNetworkImage() {
    MaterialTheme {
        NetworkImage(MovieSchema.mock().posterPath)
    }
}