package org.sco.movieratings.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.onEach
import org.sco.movieratings.repository.MovieRepository
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.api.response.MoviesResponse
import javax.inject.Inject

class MovieListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val loader = MutableLiveData<Boolean>()
    val movies: MutableLiveData<Result<List<MoviesResponse>>> = MutableLiveData()
    val loadingState = MutableLiveData<Boolean>()
    val moviesLoadError = MutableLiveData<Boolean>()

    val popularMovies = liveData<Result<List<MovieSchema>>> {
        loader.postValue(true)
        emitSource(movieRepository.getPopularMovies()
            .onEach {
                loader.postValue(false)
            }
            .asLiveData()
        )
    }

    val topRatedMovies = liveData<Result<List<MovieSchema>>> {
        loader.postValue(true)
        emitSource(movieRepository.getTopRatedMovies()
            .onEach {
                loader.postValue(false)
            }
            .asLiveData()
        )
    }

    val favoriteMovies = liveData<List<MovieSchema>> {
        loader.postValue(false)
        emitSource(movieRepository.getFavoriteMovies().asLiveData())
    }
}

@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieListViewModel(movieRepository) as T
    }
}

