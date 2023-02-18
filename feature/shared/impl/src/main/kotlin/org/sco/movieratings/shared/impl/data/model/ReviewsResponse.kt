package org.sco.movieratings.shared.impl.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewsResponse(
    @field:Json(name = "results") val reviews: List<Review>? = null
)

@JsonClass(generateAdapter = true)
data class Review(
    @field:Json(name = "id") var id: String? = null,
    @field:Json(name = "author") var author: String? = null,
    @field:Json(name = "content") var content: String? = null,
    @field:Json(name = "url") var url: String? = null
)