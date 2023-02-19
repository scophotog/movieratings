package org.sco.movieratings.moviedetails.ui.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sco.movieratings.moviedetails.api.MovieDetailInteractor
import org.sco.movieratings.shared.api.MovieListItem
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieInteractor: MovieDetailInteractor
) : ViewModel() {


    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun checkIsFavorite(movieId: Int) = viewModelScope.launch {
        _isFavorite.value = movieInteractor.isFavorite(movieId)
    }

    fun getMovie(movieId: Int): Flow<MovieListItem> = flow {
        emit(movieInteractor.getMovie(movieId))
    }

    fun onFavoriteClick(movieId: Int) {
        viewModelScope.launch {
            if (movieInteractor.isFavorite(movieId)) {
                movieInteractor.removeFavorite(movieId)
            } else {
                movieInteractor.addFavorite(movieId)
            }
            _isFavorite.emit(!_isFavorite.value)
        }
    }
}