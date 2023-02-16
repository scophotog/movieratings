package org.sco.movieratings.movielist.repository

import org.sco.movieratings.db.MovieDao
import org.sco.movieratings.movielist.api.MovieListItem
import org.sco.movieratings.movielist.api.MovieListRepository
import org.sco.movieratings.movielist.data.model.MoviesResponse
import org.sco.movieratings.movielist.data.remote.MovieListRest
import org.sco.movieratings.movielist.mapper.MovieMapper
import org.sco.movieratings.movielist.mapper.MovieSchemaMapper
import org.sco.movieratings.network.apiCall
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MovieListRepositoryImpl @Inject constructor(
    private val service: MovieListRest,
    private val mapper: MovieMapper,
    @Named("movie_list") private val movieDao: MovieDao,
    private val movieSchemaMapper: MovieSchemaMapper
) : MovieListRepository {

    override suspend fun getPopularMovies(refresh: Boolean): List<MovieListItem> =
        doApiCall { service.getPopularMovies() }

    override suspend fun getTopRatedMovies(refresh: Boolean): List<MovieListItem> =
        doApiCall { service.getTopRatedMovies() }

    override suspend fun getFavoriteMovies(): List<MovieListItem> =
        movieDao.getFavorites().run {
            movieSchemaMapper(this)
        }

    private suspend fun doApiCall(serviceApiCall: suspend () -> Response<MoviesResponse>): List<MovieListItem> {
        val result = mutableListOf<MovieListItem>()
        apiCall { serviceApiCall() }
            .onSuccess { response ->
                mapper(response.movies ?: emptyList()).also {
                    result.addAll(it)
                }
            }
            .onFailure {
                // Womp womp
            }
        return result
    }


}