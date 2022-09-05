package org.sco.movieratings.movielist.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import org.sco.movieratings.R
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.movielist.MovieListType
import org.sco.movieratings.movielist.MovieListViewModel
import org.sco.movieratings.utility.MovieListViewState

@Composable
fun MovieList(
    modifier: Modifier = Modifier, movieListType: MovieListType, onItemClick: (Int) -> Unit
) {
    val viewModel: MovieListViewModel = hiltViewModel()
    val viewState by remember(viewModel, movieListType) {
        viewModel.fetchMovieList(
            movieListType
        )
    }.collectAsState(initial = MovieListViewState.Loading)

    MovieList(
        viewState = viewState,
        onMovieClick = onItemClick,
        modifier = modifier
    )
}

@Composable
private fun MovieList(
    modifier: Modifier = Modifier,
    viewState: MovieListViewState,
    onMovieClick: (Int) -> Unit
) {
    Crossfade(viewState, modifier = modifier.fillMaxSize()) { state ->
        when (state) {
            MovieListViewState.Empty -> MovieListError(
                errorMessage = "Oh No no movies",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
            is MovieListViewState.Loaded -> MovieGridList(
                movieList = state.moveList,
                onMovieClick = onMovieClick
            )
            MovieListViewState.Loading -> CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
fun MovieGridList(movieList: List<MovieSchema>, onMovieClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movieList) { movie ->
            MovieItem(movie, onMovieClick)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, heightDp = 180)
@Composable
fun MovieListPreview() {
    MaterialTheme {
        MovieGridList(
            movieList = listOf(
                MovieSchema.mock(), MovieSchema.mock(), MovieSchema.mock(), MovieSchema.mock()
            ),
            onMovieClick = { }
        )
    }
}

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
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        AsyncImage(
            model = movie.posterPath,
            contentDescription = movie.title,
            placeholder = painterResource(id = R.drawable.movie_poster),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun MovieListError(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = errorMessage,
        style = MaterialTheme.typography.h2,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewMovieListError() {
    MovieListError(errorMessage = "Uh oh no movies")
}