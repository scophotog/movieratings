package org.sco.movieratings.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.sco.movieratings.R
import org.sco.movieratings.movielist.api.MovieListType
import org.sco.movieratings.movielist.ui.movielist.compose.MovieList
import org.sco.movieratings.ui.theme.AppTheme

fun NavGraphBuilder.addHomeGraph(
    onMovieSelected: (Int, NavBackStackEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    composable(MovieListSections.FAVORITE_MOVIES.route) { from ->
        MovieList(
            movieListType = MovieListType.FAVORITE,
            onItemClick = { id -> onMovieSelected(id, from) },
            modifier = modifier
        )
    }
    composable(MovieListSections.TOP_MOVIES.route) { from ->
        MovieList(
            movieListType = MovieListType.TOP,
            onItemClick = { id -> onMovieSelected(id, from) },
            modifier = modifier
        )
    }
    composable(MovieListSections.POPULAR_MOVIES.route) { from ->
        MovieList(
            movieListType = MovieListType.POPULAR,
            onItemClick = { id -> onMovieSelected(id, from) },
            modifier = modifier
        )
    }
}

enum class MovieListSections(
    var route: String, @DrawableRes var icon: Int, @StringRes var title: Int
) {
    TOP_MOVIES(
        "${MainDestinations.HOME_ROUTE}/${MovieListType.TOP}",
        R.drawable.ic_top_rated,
        R.string.bottom_nav_top
    ),
    POPULAR_MOVIES(
        "${MainDestinations.HOME_ROUTE}/${MovieListType.POPULAR}",
        R.drawable.ic_most_popular,
        R.string.bottom_nav_popular
    ),
    FAVORITE_MOVIES(
        "${MainDestinations.HOME_ROUTE}/${MovieListType.FAVORITE}",
        R.drawable.ic_my_favorite,
        R.string.bottom_nav_favorite
    )
}

@Composable
fun MovieRatingsBottomBar(
    tabs: Array<MovieListSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit
) {
    val currentSection = tabs.first { it.route == currentRoute }

    BottomNavigation {
        tabs.forEach { section ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = section.icon),
                        contentDescription = stringResource(section.title)
                    )
                },
                label = { Text(text = stringResource(section.title)) },
                alwaysShowLabel = true,
                selected = section == currentSection,
                onClick = { navigateToRoute(section.route) }
            )
        }
    }
}

@Preview
@Composable
private fun MovieRatingsBottomNavPreview() {
    AppTheme {
        MovieRatingsBottomBar(
            tabs = MovieListSections.values(),
            currentRoute = MovieListSections.TOP_MOVIES.route,
            navigateToRoute = {}
        )
    }
}