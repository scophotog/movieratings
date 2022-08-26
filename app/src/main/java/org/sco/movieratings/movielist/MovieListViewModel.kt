package org.sco.movieratings.movielist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.sco.movieratings.repository.MovieRepository
import org.sco.movieratings.utility.MovieListViewState
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    fun fetchMovieList(listType: MovieListType) : Flow<MovieListViewState> = flow {
        movieRepository.getMovieList(listType).collect { movieList ->
            val state = if (movieList.isNotEmpty()) {
                MovieListViewState.Loaded(movieList)
            } else {
                MovieListViewState.Empty
            }
            emit(state)
        }
    }
}

