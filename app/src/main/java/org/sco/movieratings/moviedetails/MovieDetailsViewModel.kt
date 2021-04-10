package org.sco.movieratings.moviedetails

import androidx.annotation.UiThread
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.sco.movieratings.api.MoviesService
import org.sco.movieratings.api.response.Preview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.repository.MovieRepository
import org.sco.movieratings.db.MovieSchema
import javax.inject.Inject

class MovieDetailsViewModel (
    private val moviesService: MoviesService,
    private val movieRepository: MovieRepository
) : ViewModel() {

//    private val _isFavorite : MutableLiveData<Boolean> = MutableLiveData()
//    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _favoriteMovies : LiveData<List<MovieSchema>> =
        movieRepository.getFavoriteMovies().asLiveData(viewModelScope.coroutineContext)
    val favoriteMovies: LiveData<List<MovieSchema>> = _favoriteMovies

    fun getReviews(movieId: Int) = liveData<Result<List<Review>>> {
        emitSource(moviesService.getMovieReviews(movieId).asLiveData())
    }

    fun getPreviews(movieId: Int) = liveData<Result<List<Preview>>> {
        emitSource(moviesService.getMoviePreviews(movieId).asLiveData())
    }

//    fun checkIfFavorite(movieSchema: MovieSchema) = viewModelScope.launch{
//        _isFavorite.postValue(movieRepository.isFavorite(movieSchema).first() != null)
//    }

    fun checkIfFavorite(movieSchema: MovieSchema) = liveData<MovieSchema?> {
        emitSource(movieRepository.isFavorite(movieSchema).asLiveData())
    }

    fun addToFavorite(movieSchema: MovieSchema) {
        viewModelScope.launch {
            movieRepository.addToFavorites(movieSchema)
//            _isFavorite.postValue(true)
        }
    }

    fun removeFromFavorites(movie: MovieSchema) {
        viewModelScope.launch {
            movieRepository.removeFromFavorites(movie)
//            _isFavorite.postValue(false)
        }
    }

}

class MovieDetailsViewModelFactory @Inject constructor(
    private val moviesService: MoviesService,
    private val movieRepository: MovieRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(moviesService, movieRepository) as T
    }
}