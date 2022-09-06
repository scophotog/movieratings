package org.sco.movieratings.utility

import org.sco.movieratings.db.MovieSchema

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
    object InProgress : Result<Nothing>()
    object Empty : Result<Nothing>()

    val extractData: T?
        get() = when(this) {
            is Success -> data
            is Error -> null
            is InProgress -> null
            is Empty -> null
        }
}

sealed class MovieListViewState {
    object Loading : MovieListViewState()
    data class Loaded(val moveList: List<MovieSchema>) : MovieListViewState()
    object Empty: MovieListViewState()
}
