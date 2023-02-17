package org.sco.movieratings.movielist.ui.movielist.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.sco.movieratings.movielist.api.MovieListInteractor
import org.sco.movieratings.movielist.api.MovieListType
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListInteractor: MovieListInteractor
) : ViewModel() {

    fun fetchMovieList(listType: MovieListType): Flow<MovieListViewState> = flow {
        val movieList = when (listType) {
            MovieListType.POPULAR -> movieListInteractor.getPopularMovies()
            MovieListType.TOP -> movieListInteractor.getTopRatedMovies()
            MovieListType.FAVORITE -> movieListInteractor.getFavoriteMovies()
        }
        val state = if (movieList.isNotEmpty()) {
            MovieListViewState.Loaded(movieList)
        } else {
            MovieListViewState.Empty
        }
        emit(state)
    }
//    fun fetchMovieList(listType: MovieListType) : Flow<MovieListViewState> = flow {
//        movieRepository.getMovieList(listType).collect { movieList ->
//            val state = if (movieList.isNotEmpty()) {
//                MovieListViewState.Loaded(movieList)
//            } else {
//                MovieListViewState.Empty
//            }
//            emit(state)
//        }
//    }
}

