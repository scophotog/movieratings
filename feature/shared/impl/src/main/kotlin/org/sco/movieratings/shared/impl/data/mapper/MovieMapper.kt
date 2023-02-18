package org.sco.movieratings.shared.impl.data.mapper

import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.shared.api.MovieListItem
import org.sco.movieratings.shared.impl.data.model.Movie
import javax.inject.Inject
import javax.inject.Named

class MovieMapper @Inject constructor(
    @Named("image_path") private val imagePath: String,
    @Named("backdrop_path") private val backdropPath: String
) : Function1<List<Movie>, List<MovieListItem>> {
    override fun invoke(movieRawList: List<Movie>): List<MovieListItem> {
        return movieRawList.map {
            MovieListItem(
                id = it.id ?: -1,
                title = it.title ?: "",
                posterPath = it.posterPath?.run { imagePath + this } ?: "",
                overview = it.overview ?: "",
                releaseDate = it.releaseDate ?: "",
                popularity = it.popularity ?: -1.00,
                voteAverage = it.voteAverage ?: -1.00,
                backdropPath = it.backdropPath?.run { backdropPath + this } ?: ""
            )
        }
    }
}

class MovieSchemaMapper @Inject constructor(
    @Named("image_path") private val imagePath: String
) : Function1<List<MovieSchema>, List<MovieListItem>> {
    override fun invoke(movieRawList: List<MovieSchema>): List<MovieListItem> {
        return movieRawList.map {
            MovieListItem(
                id = it.id,
                title = it.title,
                posterPath = it.posterPath.run { imagePath + this },
            )
        }
    }
}