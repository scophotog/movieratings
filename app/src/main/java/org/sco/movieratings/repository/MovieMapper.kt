package org.sco.movieratings.repository

import org.sco.movieratings.api.response.Movie
import org.sco.movieratings.db.MovieSchema
import javax.inject.Inject

class MovieMapper @Inject constructor(): Function1<List<Movie>, List<MovieSchema>> {
    override fun invoke(movieRawList: List<Movie>): List<MovieSchema> {
        return movieRawList.map {
            MovieSchema(
                id = it.id ?: -1,
                title = it.title?: "",
                posterPath = it.posterPath ?: "",
                overview = it.overview ?: "",
                releaseDate = it.releaseDate ?: "",
                popularity = it.popularity ?: -1.00,
                voteAverage = it.voteAverage  ?: -1.00,
            )
        }
    }
}