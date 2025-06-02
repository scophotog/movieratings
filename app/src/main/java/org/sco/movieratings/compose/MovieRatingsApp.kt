package org.sco.movieratings.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.sco.movieratings.designsystem.ui.theme.AppTheme
import org.sco.movieratings.moviedetails.ui.moviedetails.compose.MovieDetailsScreen

@Composable
fun MovieApp() {
    AppTheme {
        val appState = rememberMovieRatingsAppState()
        Scaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    MovieRatingsBottomBar(
                        tabs = appState.bottomBarTabs,
                        currentRoute = appState.currentRoute!!,
                        navigateToRoute = appState::navigateToBottomBarRoute
                    )
                }
            }
        ) { innerPaddingModifier  ->
            NavHost(
                navController = appState.navController,
                startDestination = MainDestinations.HOME_ROUTE,
                modifier = Modifier.padding(innerPaddingModifier)
            ) {
                movieRatingsNavGraph(
                    onMovieSelected = appState::navigateToMovieDetail,
                    upPress = appState::upPress
                )
            }
        }
    }
}

private fun NavGraphBuilder.movieRatingsNavGraph(
    onMovieSelected: (Int, NavBackStackEntry) -> Unit,
    upPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = MovieListSections.TOP_MOVIES.route
    ) {
        addHomeGraph(onMovieSelected)
    }
    composable(
        route = "${MainDestinations.MOVIE_DETAIL_ROUTE}/{${MainDestinations.MOVIE_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.MOVIE_ID_KEY) { type = NavType.IntType })
    ) { navBackStackEntry ->
        val arguments = requireNotNull(navBackStackEntry.arguments)
        MovieDetailsScreen(
            movieId = arguments.getInt(MainDestinations.MOVIE_ID_KEY),
            onNavigateUp = upPress
        )
    }
}