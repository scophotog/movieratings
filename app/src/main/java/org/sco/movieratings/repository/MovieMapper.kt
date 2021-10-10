package org.sco.movieratings.repository

import org.sco.movieratings.api.response.Movie
import org.sco.movieratings.db.MovieSchema
import javax.inject.Inject
import javax.inject.Named

class MovieMapper @Inject constructor(@Named("image_path") private val imagePath: String): Function1<List<Movie>, List<MovieSchema>> {
    override fun invoke(movieRawList: List<Movie>): List<MovieSchema> {
        return movieRawList.map {
            MovieSchema(
                id = it.id ?: -1,
                title = it.title?: "",
                posterPath = it.posterPath?.run { imagePath + this } ?: "",
                overview = it.overview ?: "",
                releaseDate = it.releaseDate ?: "",
                popularity = it.popularity ?: -1.00,
                voteAverage = it.voteAverage?.toFloat() ?: -1.00F,
            )
        }
    }
}