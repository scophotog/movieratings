package org.sco.movieratings.api.response

import com.google.gson.annotations.SerializedName
import org.sco.movieratings.api.models.Review

data class ReviewsResponse(
    @SerializedName("results") val reviews: List<Review>
) {}