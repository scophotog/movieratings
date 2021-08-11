package org.sco.movieratings.movielist

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.repository.FavoritesRepository
import org.sco.movieratings.repository.MovieRepository
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val moviesLoadError = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val popularMovies = liveData<List<MovieSchema>> {
        _isLoading.postValue(true)
        emitSource(
            movieRepository.getPopularMovies().onCompletion {
                _isLoading.postValue(false)
            }.asLiveData()
        )
    }

    val topRatedMovies = liveData<List<MovieSchema>> {
        _isLoading.postValue(true)
        emitSource(
            movieRepository.getTopRatedMovies().onCompletion {
                _isLoading.postValue(false)
            }.asLiveData()
        )
    }

    val favoriteMovies = liveData<List<MovieSchema>> {
        _isLoading.postValue(false)
        emitSource(
            favoritesRepository.getFavoriteMovies().onCompletion {
                _isLoading.postValue(false)
            }.asLiveData()
        )
    }
}

