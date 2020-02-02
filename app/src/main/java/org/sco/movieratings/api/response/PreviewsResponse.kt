package org.sco.movieratings.api.response

import com.google.gson.annotations.SerializedName
import org.sco.movieratings.api.models.Preview

data class PreviewsResponse(
    @SerializedName("results") val previews: List<Preview>
) {
}