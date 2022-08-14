package org.sco.movieratings.movielist.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import org.sco.movieratings.MovieListScreen
import org.sco.movieratings.R
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.movielist.MovieListType
import org.sco.movieratings.movielist.MovieListViewModel
import org.sco.movieratings.utility.MovieListViewState


@Composable
fun MovieList(
    modifier: Modifier = Modifier, movieListType: MovieListType, onItemClick: (Int) -> Unit, navController: NavHostController
) {
    MovieListLoader(modifier = modifier, movieListType = movieListType, onItemClick = onItemClick, navController = navController)
}

@Composable
private fun MovieListLoader(
    modifier: Modifier = Modifier,
    movieListType: MovieListType,
    onItemClick: (Int) -> Unit,
    viewModel: MovieListViewModel = hiltViewModel(),
    navController: NavHostController// Because view model is annotated with @HiltViewModel you must use hiltViewModel and not viewModel()
) {
    val (movieListTypeSaveable) = rememberSaveable { mutableStateOf(movieListType) }
    val viewState by remember(viewModel, movieListTypeSaveable) {
        viewModel.fetchMovieList(
            movieListTypeSaveable
        )
    }
        .collectAsState(initial = MovieListViewState.Loading)

    MovieListScaffold(
        viewState = viewState,
        modifier = modifier,
        onItemClick = onItemClick,
        navController = navController
    )
}

@Composable
internal fun MovieListScaffold(
    modifier: Modifier = Modifier,
    viewState: MovieListViewState,
    onItemClick: (Int) -> Unit,
    navController: NavHostController
) {
    Scaffold(
        topBar = { TopBar() },
        modifier = modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) },
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Crossfade(viewState) { state ->
                when (state) {
                    MovieListViewState.Empty -> MovieListError(errorMessage = "Oh No no movies")
                    is MovieListViewState.Loaded -> MovieListLoaded(
                        data = state.moveList,
                        selectMovie = onItemClick
                    )
                    MovieListViewState.Loading -> Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TopBar(title: String = stringResource(id = R.string.app_name)) {
    TopAppBar(
        title = { Text(text = title, fontSize = 18.sp) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar()
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
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        MovieListScreen.PopularMovies,
        MovieListScreen.TopMovies,
        MovieListScreen.FavoriteMovies
    )

    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title) },
                selectedContentColor = MaterialTheme.colors.onPrimary,
                unselectedContentColor = MaterialTheme.colors.onPrimary.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
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

@Composable
fun MovieListError(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.h2,
            modifier = Modifier.align((Alignment.CenterHorizontally)),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun PreviewMovieListError() {
    MovieListError(errorMessage = "Uh oh no movies")
}