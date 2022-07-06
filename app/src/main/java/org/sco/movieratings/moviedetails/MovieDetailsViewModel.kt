package org.sco.movieratings.moviedetails

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sco.movieratings.api.response.Preview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.repository.MovieRepository
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    fun getReviews(movieId: Int): LiveData<List<Review>> = liveData {
        emitSource(movieRepository.getMovieReviews(movieId).asLiveData())
    }

    fun getPreviews(movieId: Int): LiveData<List<Preview>> = liveData {
        emitSource(movieRepository.getMoviePreviews(movieId).asLiveData())
    }

    private val _isFavorite: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFavorite : StateFlow<Boolean> = _isFavorite

    fun checkIfFavorite(movieSchema: MovieSchema): LiveData<Boolean> = liveData {
        movieRepository.isFavorite(movieSchema).collect {
            emit(it)
            _isFavorite.value = it
        }
    }

    fun addToFavorite(movieSchema: MovieSchema) {
        viewModelScope.launch {
            movieRepository.addToFavorites(movieSchema)
            _isFavorite.value = true
        }
    }

    fun removeFromFavorites(movieSchema: MovieSchema) {
        viewModelScope.launch {
            movieRepository.removeFromFavorites(movieSchema)
            _isFavorite.value = false
        }
    }

}