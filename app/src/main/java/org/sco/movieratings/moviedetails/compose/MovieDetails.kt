package org.sco.movieratings.moviedetails.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.sco.movieratings.R
import org.sco.movieratings.api.response.MoviePreview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.moviedetails.MovieDetailsViewModel

@Composable
fun MovieDetailsScreen(movieId: Int, onNavigateUp: () -> Unit) {
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

    val movieReviews by remember(movieDetailsViewModel, movieId) {
        movieDetailsViewModel.getReviews(movieId)
    }.collectAsState(initial = null)

    val moviePreviews by remember(movieDetailsViewModel, movieId) {
        movieDetailsViewModel.getPreviews(movieId)
    }.collectAsState(initial = null)

    val isFavorite by remember(movieDetailsViewModel, movieId) {
        movieDetailsViewModel.isFavorite
    }.collectAsState()

    LaunchedEffect(true) {
        movieDetailsViewModel.checkIsFavorite(movieId)
    }

    movieDetail?.let {
        MovieDetailsScreen(
            movieSchema = it,
            onNavigateUp = onNavigateUp,
            reviewList = movieReviews ?: listOf(),
            previewList = moviePreviews ?: listOf(),
            isFavorite = isFavorite,
            onFavoriteIconClick = { movieDetailsViewModel.onFavoriteClick(it) })
    }
}

@Composable
fun MovieDetailsScreen(
    movieSchema: MovieSchema,
    reviewList: List<Review>,
    previewList: List<MoviePreview>,
    onNavigateUp: () -> Unit,
    isFavorite: Boolean,
    onFavoriteIconClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        BannerImage(movieSchema = movieSchema, contentDescription = "")
        MovieDetailsTopBar(onNavigateUp = onNavigateUp, modifier = Modifier.zIndex(1f))// By putting this here you can over lay the top bar
        Scaffold(
            backgroundColor = Color.Transparent,
            floatingActionButton = { FavoriteIcon(isFavorite, onFavoriteIconClick) }
        ) {
            LazyColumn(
                modifier = Modifier.padding(it),
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    MovieDetailsSummary(movieSchema)
                }
                item {
                    HeaderSection("Reviews")
                }
                items(reviewList) { review ->
                    MovieReview(review)
                }
                item {
                    HeaderSection("Previews")
                }
                items(previewList) { preview ->
                    MoviePreview(
                        moviePreview = preview,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun MovieDetailsSummary(movieSchema: MovieSchema) {
    Spacer(Modifier.height(125.dp))
    Row {
        Column(Modifier.padding(end = 4.dp)) {
            MovieDetailsPoster(movieSchema = movieSchema)
        }
        Column(Modifier.padding(start = 4.dp)) {
            MovieDetailsTextLayout(movieSchema = movieSchema)
        }
    }
}

@Composable
fun MovieDetailsPoster(movieSchema: MovieSchema) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(movieSchema.posterPath)
            .build(),
        placeholder = painterResource(id = R.drawable.movie_poster),
        contentDescription = null
    )
}

@Composable
fun MovieDetailsTopBar(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Movie Details", fontSize = 18.sp) },
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
        contentColor = MaterialTheme.colors.onPrimary,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        modifier = modifier
    )
}

@Composable
fun MovieDetailsTextLayout(movieSchema: MovieSchema) {
    Column {
        Text(text = movieSchema.title, style = MaterialTheme.typography.h4)
        Text(text = "Release Date ${movieSchema.releaseDate}")
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
        )
        Text(text = "Overview".uppercase(), style = MaterialTheme.typography.h6)
        Text(text = movieSchema.overview, style = MaterialTheme.typography.body1)
    }
}

@Preview
@Composable
fun MovieDetailsTextLayout() {
    MovieDetailsTextLayout(movieSchema = TestData)
}

@Composable
fun FavoriteIcon(isFavorite: Boolean, onClick: () -> Unit) {
    FloatingActionButton(onClick = { onClick() }) {
        Icon(
            painter = if (isFavorite) painterResource(R.drawable.ic_is_favorite) else painterResource(
                R.drawable.ic_not_favorite
            ),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun PreviewMovieDetails(@PreviewParameter(LoremIpsum::class) overview: String) {
    MovieDetailsScreen(
        movieSchema = MovieSchema(
            id = 1,
            title = "Movie Title",
            posterPath = "url",
            backdropPath = "url",
            popularity = 5.0,
            voteAverage = 5.0f,
            releaseDate = "01/01/2022",
            overview = overview
        ),
        onNavigateUp = {},
        onFavoriteIconClick = {},
        reviewList = listOf(),
        previewList = listOf(),
        isFavorite = true
    )
}

@Composable
fun BannerImage(
    modifier: Modifier = Modifier,
    movieSchema: MovieSchema,
    contentDescription: String
) {
    Box(modifier.wrapContentHeight()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movieSchema.backdropPath)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            alignment = Alignment.TopStart,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colors.background
                        ),
                        startY = 50f
                    )
                )
        )
    }
}

@Preview(widthDp = 780)
@Composable
fun PreviewBannerImageLocal() {
    Box(Modifier.wrapContentHeight()) {
        AsyncImage(
            model = "",
            placeholder = painterResource(R.drawable.backdrop_780w),
            contentDescription = null,
            alignment = Alignment.TopStart,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colors.background
                        ),
                        startY = 50f
                    )
                )
        )
    }
}

@Preview
@Composable
fun PreviewBannerImage() {
    BannerImage(movieSchema = TestData, contentDescription = "")
}

private val TestData = MovieSchema(
    id = 1,
    title = "Fancy Movie",
    posterPath = "url",
    backdropPath = "url",
    popularity = 5.0,
    voteAverage = 5.0f,
    releaseDate = "01/01/2022",
    overview = "Bunch of overview text"
)