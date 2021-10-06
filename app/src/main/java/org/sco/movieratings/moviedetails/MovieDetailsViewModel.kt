package org.sco.movieratings.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sco.movieratings.api.response.Preview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.repository.FavoritesRepository
import org.sco.movieratings.repository.MovieRepository
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    fun getReviews(movieId: Int) = liveData<List<Review>> {
        emitSource(movieRepository.getMovieReviews(movieId).asLiveData())
    }

    fun getPreviews(movieId: Int) = liveData<List<Preview>> {
        emitSource(movieRepository.getMoviePreviews(movieId).asLiveData())
    }

    fun checkIfFavorite(movieSchema: MovieSchema) = liveData<MovieSchema?> {
        emitSource(favoritesRepository.isFavorite(movieSchema).asLiveData())
    }

    fun addToFavorite(movieSchema: MovieSchema) {
        viewModelScope.launch {
            favoritesRepository.addToFavorites(movieSchema)
        }
    }

    fun removeFromFavorites(movie: MovieSchema) {
        viewModelScope.launch {
            favoritesRepository.removeFromFavorites(movie)
        }
    }

}