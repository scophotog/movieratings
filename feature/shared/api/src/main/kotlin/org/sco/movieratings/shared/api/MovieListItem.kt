package org.sco.movieratings.shared.api

data class MovieListItem(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val overview: String? = null,
    val releaseDate: String? = null,
    val popularity: Double? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val backdropPath: String? = null,
    val previewList: MutableList<MoviePreviewItem> = mutableListOf(),
    val reviewList: MutableList<MovieReviewItem> = mutableListOf()
)
