package org.sco.movieratings.moviedetails.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import org.sco.movieratings.R
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.moviedetails.MovieDetailsViewModel

@Composable
fun MovieDetails(movieId: Int, onNavigateUp: () -> Unit) {
    MovieDetailsLoader(movieId = movieId, onNavigateUp = onNavigateUp)
}

@Composable
fun MovieDetailsLoader(
    movieId: Int,
    movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val movieDetail by remember(movieDetailsViewModel, movieId) {
        movieDetailsViewModel.getMovie(movieId)
    }.collectAsState(initial = null)

    MoviePosterDetails(movieSchema = movieDetail, onNavigateUp)
}


@Preview
@Composable
fun PreviewMovieDetails() {
    MovieDetailsLoader(movieId = 1, onNavigateUp = {})
}

@Composable
fun MoviePosterDetails(movieSchema: MovieSchema?, onNavigateUp: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = movieSchema?.title ?: "Movie Details", fontSize = 18.sp) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = { FavoriteIcon() }
    ) { innerPadding ->
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)) {
            movieSchema?.let {
                BannerImage(url = it.backdropPath!!, contentDescription = "")
                Row {
                    Column {
                        AsyncImage(model = it.posterPath, contentDescription = null)
                    }
                    Column {
                        Text(text = it.title, style = MaterialTheme.typography.h4)
                        Text(it.releaseDate)
                        Text(text = it.overview, style = MaterialTheme.typography.body1)
                    }
                }
            }

        }
    }
}

@Composable
fun FavoriteIcon() {
    FloatingActionButton(onClick = { }) {
        Icon(painter = painterResource(R.drawable.ic_my_favorite), contentDescription = null)
    }
}

@Preview
@Composable
fun PreviewMoviePosterDetails() {
    MoviePosterDetails(
        MovieSchema(
            id = 1,
            title = "Title",
            posterPath = "url",
            backdropPath = "url",
            popularity = 5.0,
            voteAverage = 5.0f,
            releaseDate = "01/01/2022",
            overview = "Stuff"
        )
    ) {}
}

@Composable
fun BannerImage(url: String, contentDescription: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.FillWidth,
        placeholder = painterResource(id = R.drawable.backdrop_sample),
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewBannerImage() {
    BannerImage(url = "", contentDescription = "")
}