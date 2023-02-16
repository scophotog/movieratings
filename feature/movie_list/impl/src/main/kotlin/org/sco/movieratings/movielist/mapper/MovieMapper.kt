package org.sco.movieratings.movielist.mapper

import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.movielist.api.MovieListItem
import org.sco.movieratings.movielist.data.model.Movie
import javax.inject.Inject
import javax.inject.Named

class MovieMapper @Inject constructor(
    @Named("image_path") private val imagePath: String
) : Function1<List<Movie>, List<MovieListItem>> {
    override fun invoke(movieRawList: List<Movie>): List<MovieListItem> {
        return movieRawList.map {
            MovieListItem(
                id = it.id ?: -1, // TODO this bad
                title = it.title ?: "",
                posterPath = it.posterPath?.run { imagePath + this } ?: ""
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
                title = it.title ?: "",
                posterPath = it.posterPath.run { imagePath + this } ?: "",
            )
        }
    }
}