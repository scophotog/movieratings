package org.sco.movieratings.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.sco.movieratings.core.model.data.MovieReviewItem

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

fun Review.toReviewItem() =
    MovieReviewItem(
        id = id,
        author = author,
        url = url,
        content = content
    )
