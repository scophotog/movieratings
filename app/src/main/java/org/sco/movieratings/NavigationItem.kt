package org.sco.movieratings

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object TopMovies : NavigationItem("topMovies", R.drawable.ic_top_rated, "Top")
    object PopularMovies : NavigationItem("popularMovies", R.drawable.ic_most_popular, "Popular")
    object FavoriteMovies : NavigationItem("favoriteMovies", R.drawable.ic_my_favorite, "Favorites")
}