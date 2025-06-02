package org.sco.movieratings.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.sco.movieratings.core.model.data.MovieListItem

@JsonClass(generateAdapter = true)
data class MoviesResponse(
    @field:Json(name = "results") val movies: List<Movie>? = null,
    @field:Json(name = "total_results") val totalMovieCount: Int? = null,
    @field:Json(name = "total_pages") val totalPageCount: Int? = null
)

@JsonClass(generateAdapter = true)
class Movie(
    @field:Json(name = "title") var title: String? = null,
    @field:Json(name = "id") var id: Int? = null,
    @field:Json(name = "poster_path") var posterPath: String? = null,
    @field:Json(name = "overview") var overview: String? = null,
    @field:Json(name = "release_date") var releaseDate: String? = null,
    @field:Json(name = "popularity") var popularity: Double? = null,
    @field:Json(name = "vote_average") var voteAverage: Double? = null,
    @field:Json(name = "vote_count") var voteCount: Int? = null,
    @field:Json(name = "backdrop_path") var backdropPath: String? = null
)

fun Movie.toMovieListItem() =
    MovieListItem(
        id = id ?: -1,
        title = title ?: "",
        posterPath = posterPath ?: "",
        overview = overview ?: "",
        releaseDate = releaseDate ?: "",
        popularity = popularity ?: -1.00,
        voteAverage = voteAverage ?: -1.00,
        backdropPath = backdropPath
    )