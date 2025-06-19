package org.sco.movieratings.movielist.ui.movielist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    private var _listState = MutableStateFlow<MovieListViewState>(MovieListViewState.Loading)
    val listState: StateFlow<MovieListViewState> = _listState
        .onStart {
            emit(MovieListViewState.Empty)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MovieListViewState.Loading
        )

    fun fetchMovieList(type: MovieListType) {
        viewModelScope.launch {
            _listState.value = MovieListViewState.Loading
            val movieList = when(type) {
                MovieListType.POPULAR ->  popularMoviesUseCase()
                MovieListType.TOP ->  topRatedMoviesUseCase()
                MovieListType.FAVORITE ->  favoriteMoviesUseCase()
            }
            movieList.collect {
                _listState.value = if (it.isNotEmpty()) {
                    MovieListViewState.Loaded(
                        MovieListState(
                            movieList = it,
                            type = type
                        )
                    )
                } else {
                    MovieListViewState.Empty
                }
            }
        }
    }
}

