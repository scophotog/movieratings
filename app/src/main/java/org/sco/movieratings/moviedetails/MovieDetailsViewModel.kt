package org.sco.movieratings.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sco.movieratings.api.response.MoviePreview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.repository.MovieRepository
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    fun getReviews(movieId: Int): Flow<List<Review>> =
        movieRepository.getMovieReviews(movieId)

    fun getPreviews(movieId: Int): Flow<List<MoviePreview>> =
        movieRepository.getMoviePreviews(movieId)

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun checkIsFavorite(movieId: Int) = viewModelScope.launch {
        _isFavorite.value = getMovie(movieId).firstOrNull()?.let {
            movieRepository.isFavorite(it)
        } ?: false
    }

    fun getMovie(movieId: Int): Flow<MovieSchema?> =
        movieRepository.getMovie(movieId)

    fun onFavoriteClick(movieSchema: MovieSchema) {
        viewModelScope.launch {
            val favorite = movieRepository.isFavorite(movieSchema)
            if(favorite) {
                movieRepository.removeFromFavorites(movieSchema)
            } else {
                movieRepository.addToFavorites(movieSchema)
            }
            _isFavorite.emit(!favorite)
        }
    }
}