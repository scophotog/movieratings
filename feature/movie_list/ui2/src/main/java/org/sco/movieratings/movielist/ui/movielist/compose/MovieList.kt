package org.sco.movieratings.movielist.ui.movielist.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import org.sco.movieratings.movielist.ui.movielist.R
import org.sco.movieratings.movielist.ui.movielist.viewmodel.MovieList
import org.sco.movieratings.movielist.ui.movielist.viewmodel.MovieListViewModel
import org.sco.movieratings.movielist.ui.movielist.viewmodel.MovieListViewState
import org.sco.movieratings.shared.api.MovieListItem

@Composable
fun MovieList(
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit = {}
) {
    val viewState by remember(viewModel) {
        viewModel.fetchMovieList()
    }.collectAsState(initial = MovieListViewState.Loading)

    MovieList(
        viewState = viewState,
        onMovieClick = onItemClick,
        modifier = modifier
    )
}

@Composable
fun MovieList(
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
            is MovieListViewState.Loaded -> MovieRowList(
                movieList = state.movieList,
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
fun MovieRowList(movieList: List<MovieList>, onMovieClick: (Int) -> Unit) {
    LazyColumn {
        items(movieList) {
            if (it.movieList.isNotEmpty()) {
                MovieCarousel(
                    movieListTitle = it.title,
                    movieList = it.movieList,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Composable
fun MovieCarousel(
    movieListTitle: String,
    movieList: List<MovieListItem>,
    onMovieClick: (Int) -> Unit
) {
    Column {
        Text(
            text = movieListTitle,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(start = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.testTag("MovieList")
        ) {
            items(movieList) { movie ->
                MovieItem(movie = movie, selectMovie = onMovieClick)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, heightDp = 300)
@Composable
fun MovieListPreview() {
    MaterialTheme {
        MovieCarousel(
            movieListTitle = "Sample Movies",
            movieList = listOf(
                MovieListItem(0, "Foo", null),
                MovieListItem(0, "Foo", null),
                MovieListItem(0, "Foo", null),
                MovieListItem(0, "Foo", null)
            ),
            onMovieClick = { }
        )
    }
}

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    movie: MovieListItem,
    selectMovie: (Int) -> Unit = { },
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
            contentScale = ContentScale.FillWidth,
            error = painterResource(id = R.drawable.movie_poster)
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