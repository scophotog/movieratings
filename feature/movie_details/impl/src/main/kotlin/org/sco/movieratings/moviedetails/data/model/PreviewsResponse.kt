package org.sco.movieratings.moviedetails.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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