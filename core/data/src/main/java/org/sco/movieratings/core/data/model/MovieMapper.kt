package org.sco.movieratings.core.data.model

import org.sco.movieratings.core.model.data.MovieListItem
import org.sco.movieratings.db.model.MovieSchema

fun MovieListItem.toMovieSchema() = MovieSchema(
    id = id,
    title = title,
    posterPath = posterPath ?: "",
    overview = overview ?: "",
    releaseDate = releaseDate ?: "",
    popularity = popularity ?: 0.0,
    voteAverage = voteAverage?.toFloat() ?: 0f,
    backdropPath = backdropPath
)