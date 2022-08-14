package org.sco.movieratings

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.sco.movieratings.moviedetails.compose.MovieDetails
import org.sco.movieratings.movielist.MovieListType
import org.sco.movieratings.movielist.compose.MovieList

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
                onItemClick = actions.openMovieDetails,
                navController = navController
            )
        }
        composable(MovieListScreen.TopMovies.route) {
            MovieList(
                movieListType = MovieListType.TOP,
                onItemClick = actions.openMovieDetails,
                navController = navController
            )
        }
        composable(MovieListScreen.FavoriteMovies.route) {
            MovieList(
                movieListType = MovieListType.FAVORITE,
                onItemClick = actions.openMovieDetails,
                navController = navController
            )
        }
        composable(
            route = "${Destinations.MovieDetail}/{${DestinationArgs.MovieId}}",
            arguments = listOf(navArgument(DestinationArgs.MovieId) { type = NavType.IntType })
        ) { navBackStackEntry ->
            val arguments = requireNotNull(navBackStackEntry.arguments)
            MovieDetails(
                movieId = arguments.getInt(DestinationArgs.MovieId),
                onNavigateUp = { navController.popBackStack() }
            )
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