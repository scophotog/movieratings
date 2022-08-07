package org.sco.movieratings

import androidx.annotation.DrawableRes
import org.sco.movieratings.movielist.MovieListType

sealed class MovieListScreen(var route: String, @DrawableRes var icon: Int, var title: String) {
    object TopMovies : MovieListScreen(
        "${Destinations.MovieList}/${MovieListType.TOP}",
        R.drawable.ic_top_rated,
        "Top"
    )

    object PopularMovies : MovieListScreen(
        "${Destinations.MovieList}/${MovieListType.POPULAR}",
        R.drawable.ic_most_popular,
        "Popular"
    )

    object FavoriteMovies : MovieListScreen(
        "${Destinations.MovieList}/${MovieListType.FAVORITE}",
        R.drawable.ic_my_favorite,
        "Favorites"
    )
}

object Destinations {
    const val MovieList = "movie_list"
    const val MovieDetail = "movie_detail"
}

object DestinationArgs {
    const val MovieId = "movie_id"
}