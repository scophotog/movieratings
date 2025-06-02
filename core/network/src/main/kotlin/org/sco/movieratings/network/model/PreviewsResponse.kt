package org.sco.movieratings.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.sco.movieratings.core.model.data.MoviePreviewItem

@JsonClass(generateAdapter = true)
data class PreviewsResponse(
    @field:Json(name = "results") val moviePreviews: List<MoviePreview>? = null
)

@JsonClass(generateAdapter = true)
data class MoviePreview(
    @field:Json(name = "id") var id: String? = null,
    @field:Json(name = "iso_639_1") var iso_6391: String? = null,
    @field:Json(name = "iso_3166_1") var iso_3166_1: String? = null,
    @field:Json(name = "key") var key: String? = null,
    @field:Json(name = "name") var name: String? = null,
    @field:Json(name = "site") var site: String? =null,
    @field:Json(name = "size") var size: Int? = null,
    @field:Json(name = "type") var type: String? = null
)

fun MoviePreview.toMoviePreviewItem() =
    MoviePreviewItem(
        id = id,
        key = key,
        name = name,
        site = site,
        size = size,
        type = type,
        iso_3166_1 = iso_3166_1,
        iso_6391 = iso_6391
    )