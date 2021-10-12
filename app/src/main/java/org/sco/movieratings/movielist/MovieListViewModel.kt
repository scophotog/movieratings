package org.sco.movieratings.movielist

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.repository.FavoritesRepository
import org.sco.movieratings.repository.MovieRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoritesRepository: FavoritesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val movieListType: MutableStateFlow<MovieListType> = MutableStateFlow(
        savedStateHandle.get(MOVIE_LIST_SAVED_STATE_KEY) ?: MovieListType.POPULAR
    )

    private val _viewState = MutableStateFlow(MovieViewState())
    val viewState: StateFlow<MovieViewState> get() = _viewState

    fun setMovieListType(listType: MovieListType) {
        movieListType.update { listType }
        savedStateHandle.set(MOVIE_LIST_SAVED_STATE_KEY, listType)
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(loading = true)
            val movies = when(listType) {
                MovieListType.POPULAR -> movieRepository.getPopularMovies()
                MovieListType.TOP -> movieRepository.getTopRatedMovies()
                MovieListType.FAVORITE -> favoritesRepository.getFavoriteMovies()
            }
            movies.collect {
                _viewState.value = _viewState.value.copy(loading = false, movies = it)
            }
        }
    }

    enum class MovieListType {
        POPULAR, TOP, FAVORITE
    }

    companion object {
        private const val MOVIE_LIST_SAVED_STATE_KEY = "MOVIE_LIST_SAVED_STATE_KEY"
    }

    data class MovieViewState(
        val loading: Boolean = true,
        val movies: List<MovieSchema> = emptyList()
    )
}

