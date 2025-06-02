package org.sco.movieratings.movielist.ui.movielist.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.sco.movieratings.core.domain.GetFavoriteMoviesUseCase
import org.sco.movieratings.core.domain.GetPopularMoviesUseCase
import org.sco.movieratings.core.domain.GetTopRatedMoviesUseCase
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val popularMoviesUseCase: GetPopularMoviesUseCase,
    private val topRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val favoriteMoviesUseCase: GetFavoriteMoviesUseCase,
) : ViewModel() {

    fun fetchMovieList(): Flow<MovieListViewState> = flow {
        val popular = popularMoviesUseCase().first()
        val top = topRatedMoviesUseCase().first()
        val favorites = favoriteMoviesUseCase().first()
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

