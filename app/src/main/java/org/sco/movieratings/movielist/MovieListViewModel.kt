package org.sco.movieratings.movielist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.repository.MovieRepository
import org.sco.movieratings.utility.Result
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movieListType: MutableStateFlow<MovieListType> = MutableStateFlow(
        savedStateHandle.get(MOVIE_LIST_SAVED_STATE_KEY) ?: MovieListType.POPULAR
    )
    val movieListType : StateFlow<MovieListType> =  _movieListType

    private val _viewState: MutableStateFlow<Result<List<MovieSchema>>> = MutableStateFlow(Result.Empty)
    val viewState : StateFlow<Result<List<MovieSchema>>> = _viewState

    fun setMovieListType(listType: MovieListType) {
        _movieListType.update { listType }
        savedStateHandle.set(MOVIE_LIST_SAVED_STATE_KEY, listType)
        viewModelScope.launch {
            _viewState.value = Result.InProgress
            movieRepository.getMovieList(listType).catch { e ->
                _viewState.value = Result.Error(e)
            }.collect {
                _viewState.value = Result.Success(it)
            }
        }
    }

    enum class MovieListType {
        POPULAR, TOP, FAVORITE
    }

    companion object {
        private const val MOVIE_LIST_SAVED_STATE_KEY = "MOVIE_LIST_SAVED_STATE_KEY"
    }

}

