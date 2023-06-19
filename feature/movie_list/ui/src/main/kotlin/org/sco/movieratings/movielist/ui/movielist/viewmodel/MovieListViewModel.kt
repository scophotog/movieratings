package org.sco.movieratings.movielist.ui.movielist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sco.movieratings.movielist.api.MovieListInteractor
import org.sco.movieratings.movielist.api.MovieListType
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListInteractor: MovieListInteractor
) : ViewModel() {

    private var _listState = MutableStateFlow<MovieListViewState>(MovieListViewState.Empty)
    val listState: StateFlow<MovieListViewState>
        get() = _listState.asStateFlow()

    init {
        _listState.value = MovieListViewState.Loading
    }

    fun fetchMovieList(type: MovieListType) {
        viewModelScope.launch {
            _listState.value = MovieListViewState.Loading
            val movieList = when(type) {
                MovieListType.POPULAR ->  movieListInteractor.getPopularMovies()
                MovieListType.TOP ->  movieListInteractor.getTopRatedMovies()
                MovieListType.FAVORITE ->  movieListInteractor.getFavoriteMovies()
            }
            _listState.value = if (movieList.isNotEmpty()) {
                MovieListViewState.Loaded(
                        MovieListState(
                            movieList = movieList,
                            type = type
                        )
                )
            } else {
                MovieListViewState.Empty
            }
        }
    }
}

