package org.sco.movieratings.moviedetails.api

data class MovieDetailItem(
    val id: Int,
    val title: String = "",
    val posterPath: String = "",
    val overview: String = "",
    val releaseDate: String = "",
    val popularity: Double = 0.0,
    val voteAverage: Float = 0.0f,
    val backdropPath: String? = null,
    val previewList: List<MoviePreviewItem> = listOf(),
    val reviewList: List<MovieReviewItem> = listOf()
)
