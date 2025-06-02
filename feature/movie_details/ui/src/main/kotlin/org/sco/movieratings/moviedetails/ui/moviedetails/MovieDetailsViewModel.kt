package org.sco.movieratings.moviedetails.ui.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.sco.movieratings.core.domain.AddFavoriteMovieUseCase
import org.sco.movieratings.core.domain.GetMovieUseCase
import org.sco.movieratings.core.domain.IsMovieFavoriteUseCase
import org.sco.movieratings.core.domain.RemoveFavoriteMovieUseCase
import org.sco.movieratings.core.model.data.MovieListItem
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val movieFavoriteUseCase: IsMovieFavoriteUseCase,
    private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase,
    private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase
) : ViewModel() {

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState

    fun checkIsFavorite(movieId: Int) = viewModelScope.launch {
        movieFavoriteUseCase(movieId).collect {
            _isFavorite.value = it
        }
    }

    fun getMovie(movieId: Int) {
        viewModelScope.launch {
            getMovieUseCase.invoke(movieId).collect {
                if (it == null) {
                    _uiState.value = MovieUiState.Empty
                } else {
                    _uiState.value = MovieUiState.Movie(it)
                }
            }
        }
    }

    fun onFavoriteClick(movieId: Int) {
        viewModelScope.launch {
            if (movieFavoriteUseCase(movieId).first()) {
                removeFavoriteMovieUseCase(movieId)
            } else {
                addFavoriteMovieUseCase(movieId)
            }
            _isFavorite.emit(!_isFavorite.value)
        }
    }

    sealed interface MovieUiState {
        data object Loading: MovieUiState
        data class Movie(
            val movie: MovieListItem
        ): MovieUiState
        data object Empty: MovieUiState
    }
}