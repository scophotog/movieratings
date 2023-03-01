package org.sco.movieratings.movielist.ui.movielist.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.sco.movieratings.movielist.api.MovieListInteractor
import org.sco.movieratings.movielist.api.MovieListType
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListInteractor: MovieListInteractor
) : ViewModel() {

    fun fetchMovieList(): Flow<MovieListViewState> = flow {
        val popular = movieListInteractor.getPopularMovies()
        val top = movieListInteractor.getTopRatedMovies()
        val favorites = movieListInteractor.getFavoriteMovies()
        val state = if (popular.isNotEmpty() || top.isNotEmpty() || favorites.isNotEmpty()) {
            MovieListViewState.Loaded(listOf(
                MovieList(
                    title = "Popular",
                    movieList = popular
                ),
                MovieList(
                    title = "Top",
                    movieList = top
                ),
                MovieList(
                    title = "Favorites",
                    movieList = favorites
                )
            ))
        } else {
            MovieListViewState.Empty
        }
        emit(state)
    }
}

