package org.sco.movieratings.moviedetails.ui.moviedetails.compose

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.sco.movieratings.core.model.data.MovieListItem
import org.sco.movieratings.core.model.data.MoviePreviewItem
import org.sco.movieratings.core.model.data.MovieReviewItem
import org.sco.movieratings.moviedetails.ui.moviedetails.MovieDetailsViewModel
import org.sco.movieratings.moviedetails.ui.moviedetails.R

@Composable
fun MovieDetailsScreen(movieId: Int, onNavigateUp: () -> Unit) {
    MovieDetailsLoader(movieId = movieId, onNavigateUp = onNavigateUp)
}

@Composable
fun MovieDetailsLoader(
    movieId: Int = -1,
    movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val movieDetail by remember(movieDetailsViewModel, movieId) {
        movieDetailsViewModel.getMovie(movieId)
        movieDetailsViewModel.uiState
    }.collectAsState()

    val isFavorite by remember(movieDetailsViewModel, movieId) {
        movieDetailsViewModel.isFavorite
    }.collectAsState()

    LaunchedEffect(true) {
        movieDetailsViewModel.checkIsFavorite(movieId)
    }
    val context = LocalContext.current
    when(val movie = movieDetail) {
        is MovieDetailsViewModel.MovieUiState.Empty,
        is MovieDetailsViewModel.MovieUiState.Loading -> {/* no-op */}
        is MovieDetailsViewModel.MovieUiState.Movie -> {
            MovieDetailsScreen(
                movieDetailItem = movie.movie,
                onNavigateUp = onNavigateUp,
                reviewList = movie.movie.reviewList,
                previewList = movie.movie.previewList,
                isFavorite = isFavorite,
                onFavoriteIconClick = {
                    movieDetailsViewModel.onFavoriteClick(movie.movie.id)
                    // TODO: Use a snackbar
                    if (isFavorite) {
                        Toast.makeText(context,"Removed from favorites", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context,"Added to favorites", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}

@Composable
fun MovieDetailsScreen(
    movieDetailItem: MovieListItem,
    reviewList: List<MovieReviewItem>,
    previewList: List<MoviePreviewItem>,
    onNavigateUp: () -> Unit,
    isFavorite: Boolean,
    onFavoriteIconClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        BannerImage(movieDetailItem = movieDetailItem, contentDescription = "")
        Body(movieDetailItem = movieDetailItem, reviewList = reviewList, previewList = previewList, isFavorite = isFavorite, onFavoriteClick = onFavoriteIconClick)
        UpButton(upPress = onNavigateUp)
    }
}

@Composable
private fun Body(
    modifier: Modifier = Modifier,
    movieDetailItem: MovieListItem,
    reviewList: List<MovieReviewItem>,
    previewList: List<MoviePreviewItem>,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            MovieDetailsSummary(movieDetailItem, isFavorite, onFavoriteClick)
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
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = stringResource(R.string.label_back),
            tint = Color.White
        )
    }
}

@Composable
fun MovieDetailsSummary(movieDetailItem: MovieListItem, isFavorite: Boolean, onFavoriteClick: () -> Unit) {
    Spacer(Modifier.height(125.dp))
    Row {
        Column(Modifier.padding(end = 4.dp)) {
            MovieDetailsPoster(movieDetailItem = movieDetailItem, isFavorite = isFavorite, onFavoriteClick = onFavoriteClick)
        }
        Column(Modifier.padding(start = 4.dp)) {
            MovieDetailsTextLayout(movieDetailItem = movieDetailItem)
        }
    }
}

@Composable
fun MovieDetailsPoster(movieDetailItem: MovieListItem, isFavorite: Boolean, onFavoriteClick: () -> Unit) {
    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movieDetailItem.posterPath)
                .build(),
            placeholder = painterResource(id = R.drawable.movie_poster),
            contentDescription = null
        )
        IconButton(
            onClick = { onFavoriteClick() },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).background(shape = CircleShape, color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.8f)).align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = if (isFavorite) painterResource(R.drawable.ic_is_favorite) else painterResource(
                    R.drawable.ic_not_favorite
                ),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null
            )
        }
    }
}

@Composable
fun MovieDetailsTextLayout(movieDetailItem: MovieListItem) {
    Column {
        Text(text = movieDetailItem.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Release Date ${movieDetailItem.releaseDate}")
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
        )
        Text(text = "Overview".uppercase(), style = MaterialTheme.typography.headlineSmall)
        Text(text = movieDetailItem.overview ?: "", style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview
@Composable
fun MovieDetailsTextLayoutPreview() {
    MovieDetailsTextLayout(movieDetailItem = TestData)
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewMovieDetails() {
    MaterialTheme {
        MovieDetailsScreen(
            movieDetailItem = MovieListItem(
                id = 1,
                title = "Movie Title",
                posterPath = "url",
                backdropPath = "url",
                popularity = 5.0,
                voteAverage = 5.0,
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
    movieDetailItem: MovieListItem,
    contentDescription: String
) {
    Box(modifier.wrapContentHeight()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movieDetailItem.backdropPath)
                .placeholder(R.drawable.backdrop_780w)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            alignment = Alignment.TopStart,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background
                        ),
                        startY = 50f
                    )
                )
        )
    }
}

private val TestData = MovieListItem(
    id = 1,
    title = "Fancy Movie",
    posterPath = "url",
    backdropPath = "url",
    popularity = 5.0,
    voteAverage = 5.0,
    releaseDate = "01/01/2022",
    overview = "Bunch of overview text"
)