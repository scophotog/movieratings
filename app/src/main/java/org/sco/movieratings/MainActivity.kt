package org.sco.movieratings

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sco.movieratings.movielist.MovieListType
import org.sco.movieratings.movielist.compose.MovieList

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navController) },
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) { padding ->
        NavGraph(navController = navController, modifier = Modifier.padding(padding))
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name), fontSize = 18.sp) },
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

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
//    BottomNavigationBar()
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = MovieListScreen.PopularMovies.route,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val actions = remember { Actions(navController, context) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(MovieListScreen.PopularMovies.route) {
            MovieList(
                movieListType = MovieListType.POPULAR,
                onItemClick = actions.openMovieDetails
            )
        }
        composable(MovieListScreen.TopMovies.route) {
            MovieList(
                movieListType = MovieListType.TOP,
                onItemClick = actions.openMovieDetails
            )
        }
        composable(MovieListScreen.FavoriteMovies.route) {
            MovieList(
                movieListType = MovieListType.FAVORITE,
                onItemClick = actions.openMovieDetails
            )
        }
        composable(
            route = "${Destinations.MovieDetail}/{${DestinationArgs.MovieId}}"
        ) { navBackStackEntry ->
            val arguments = requireNotNull(navBackStackEntry.arguments)
        }
    }
}

internal data class Actions(val navController: NavHostController, val context: Context) {
    val openMovieList: () -> Unit = {
        navController.navigate(Destinations.MovieList)
    }
    val openMovieDetails: (Int) -> Unit = { movieId ->
        navController.navigate("${Destinations.MovieDetail}/$movieId")
    }
}