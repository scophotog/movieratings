package org.sco.movieratings.moviedetails.interactor

import org.sco.movieratings.moviedetails.api.MovieDetailInteractor
import org.sco.movieratings.moviedetails.usecase.AddFavoriteMovieUseCase
import org.sco.movieratings.moviedetails.usecase.GetFavoriteMovieUseCase
import org.sco.movieratings.moviedetails.usecase.GetMovieUseCase
import org.sco.movieratings.moviedetails.usecase.RemoveFavoriteMovieUseCase
import org.sco.movieratings.shared.api.MovieListItem
import javax.inject.Inject

class MovieDetailsInteractorImpl @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val getFavoriteMovieUseCase: GetFavoriteMovieUseCase,
    private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
    private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase
): MovieDetailInteractor {
    override suspend fun getMovie(movieId: Int): MovieListItem =
        getMovieUseCase(movieId)

    override suspend fun isFavorite(movieId: Int): Boolean =
        getFavoriteMovieUseCase(movieId) != null

    override suspend fun addFavorite(movieId: Int) {
        addFavoriteMovieUseCase(movieId)
    }

    override suspend fun removeFavorite(movieId: Int) {
        removeFavoriteMovieUseCase(movieId)
    }
}