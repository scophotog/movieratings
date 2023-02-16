package org.sco.movieratings.movielist.interactor

import org.sco.movieratings.movielist.api.MovieListInteractor
import org.sco.movieratings.movielist.api.MovieListItem
import org.sco.movieratings.movielist.usecase.GetFavoriteMoviesUseCase
import org.sco.movieratings.movielist.usecase.GetPopularMoviesUseCase
import org.sco.movieratings.movielist.usecase.GetTopRatedMoviesUseCase
import javax.inject.Inject

class MovieListInteractorImpl @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase
): MovieListInteractor {

    override suspend fun getPopularMovies(refresh: Boolean): List<MovieListItem> =
        getPopularMoviesUseCase.invoke()

    override suspend fun getTopRatedMovies(refresh: Boolean): List<MovieListItem> =
        getTopRatedMoviesUseCase.invoke()

    override suspend fun getFavoriteMovies(): List<MovieListItem> =
        getFavoriteMoviesUseCase.invoke()
}