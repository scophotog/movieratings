package org.sco.espresso.robots

import org.sco.movieratings.R

class MovieListRobot {

    fun tapMostPopular() {
        checkMostPopularIsDisplayed()
        clickElementWithId(R.id.bn_most_popular)
    }

    fun tapTopRated() {
        checkTopRatedIsDisplayed()
        clickElementWithId(R.id.bn_top_rated)
    }

    fun tapFavorites() {
        checkFavoritesIsDisplayed()
        clickElementWithId(R.id.bn_my_favorites)
    }

    fun tapMovieInPosition(position: Int) {
        clickItemInList(R.id.movieList, position)
    }

    fun checkEmptyListDisplayed() {
        assertTextInElement(R.id.emptyView, "No movies :(")
    }

    fun checkMovieIsDisplayed() {
        assertElementIsDisplayed(R.id.moviePoster)
    }

    fun checkTitleMatches(title: String) {
        assertTitleBarTextMatches(R.id.toolbar, title)
    }

    fun checkMovieWithTitleExists(movieTitle: String) {
        assertTextInElement(R.id.movieTitle, movieTitle)
    }

    fun checkFavoritesIsDisplayed() {
        assertElementIsDisplayed(R.id.bn_my_favorites)
    }

    fun checkTopRatedIsDisplayed() {
        assertElementIsDisplayed(R.id.bn_top_rated)
    }

    fun checkMostPopularIsDisplayed() {
        assertElementIsDisplayed(R.id.bn_most_popular)
    }

}

fun movieList(f: MovieListRobot.() -> Unit) = MovieListRobot().apply { f() }