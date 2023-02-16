package org.sco.movieratings.moviedetails.api

data class MoviePreviewItem(
    var id: String? = null,
    var iso_6391: String? = null,
    var iso_3166_1: String? = null,
    var key: String? = null,
    var name: String? = null,
    var site: String? =null,
    var size: Int? = null,
    var type: String? = null
) {
}