package org.sco.movieratings.moviedetails.compose

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.sco.movieratings.AppTheme
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
    val context = LocalContext.current
    movieDetail?.let {
        MovieDetailsScreen(
            movieSchema = it,
            onNavigateUp = onNavigateUp,
            reviewList = movieReviews ?: listOf(),
            previewList = moviePreviews ?: listOf(),
            isFavorite = isFavorite,
            onFavoriteIconClick = {
                movieDetailsViewModel.onFavoriteClick(it)
                // TODO: Use a snackbar
                if (isFavorite) {
                    Toast.makeText(context,"Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,"Added to favorites", Toast.LENGTH_SHORT).show()
                }
            })
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        BannerImage(movieSchema = movieSchema, contentDescription = "")
        Body(movieSchema = movieSchema, reviewList = reviewList, previewList = previewList, isFavorite = isFavorite, onFavoriteClick = onFavoriteIconClick)
        UpButton(upPress = onNavigateUp)
    }
}

@Composable
private fun Body(
    modifier: Modifier = Modifier,
    movieSchema: MovieSchema,
    reviewList: List<Review>,
    previewList: List<MoviePreview>,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            MovieDetailsSummary(movieSchema, isFavorite, onFavoriteClick)
        }
        if (reviewList.isNotEmpty()) {
            item {
                HeaderSection("Reviews")
            }
            items(reviewList) { review ->
                MovieReview(review)
            }
        }
        if (previewList.isNotEmpty()) {
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

@Composable
private fun UpButton(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .size(36.dp)
            .background(
                color = Color.Black.copy(alpha = 0.70f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = stringResource(R.string.label_back),
            tint = Color.White
        )
    }
}

@Composable
fun MovieDetailsSummary(movieSchema: MovieSchema, isFavorite: Boolean, onFavoriteClick: () -> Unit) {
    Spacer(Modifier.height(125.dp))
    Row {
        Column(Modifier.padding(end = 4.dp)) {
            MovieDetailsPoster(movieSchema = movieSchema, isFavorite = isFavorite, onFavoriteClick = onFavoriteClick)
        }
        Column(Modifier.padding(start = 4.dp)) {
            MovieDetailsTextLayout(movieSchema = movieSchema)
        }
    }
}

@Composable
fun MovieDetailsPoster(movieSchema: MovieSchema, isFavorite: Boolean, onFavoriteClick: () -> Unit) {
    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movieSchema.posterPath)
                .build(),
            placeholder = painterResource(id = R.drawable.movie_poster),
            contentDescription = null
        )
        IconButton(
            onClick = { onFavoriteClick() },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).background(shape = CircleShape, color = MaterialTheme.colors.onSecondary.copy(alpha = 0.8f)).align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = if (isFavorite) painterResource(R.drawable.ic_is_favorite) else painterResource(
                    R.drawable.ic_not_favorite
                ),
                tint = MaterialTheme.colors.secondary,
                contentDescription = null
            )
        }
    }
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

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewMovieDetails() {
    AppTheme {
        MovieDetailsScreen(
            movieSchema = MovieSchema(
                id = 1,
                title = "Movie Title",
                posterPath = "url",
                backdropPath = "url",
                popularity = 5.0,
                voteAverage = 5.0f,
                releaseDate = "01/01/2022",
                overview = "Movie Overview"
            ),
            onNavigateUp = {},
            onFavoriteIconClick = {},
            reviewList = listOf(),
            previewList = listOf(),
            isFavorite = true
        )
    }
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
                .placeholder(R.drawable.backdrop_780w)
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