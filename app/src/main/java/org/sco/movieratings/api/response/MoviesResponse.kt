package org.sco.movieratings.api.response

import com.google.gson.annotations.SerializedName
import org.sco.movieratings.api.models.Movie

data class MoviesResponse(
    @SerializedName("results") val movies: List<Movie>,
    @SerializedName("total_results") val totalMovieCount: Int,
    @SerializedName("total_pages") val totalPageCount: Int) {}