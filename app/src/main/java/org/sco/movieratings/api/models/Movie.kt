package org.sco.movieratings.api.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(var title: String, var id: Int, var posterPath: String, var overview: String,
            var releaseDate: String, var popularity: Double, var voteAverage: Double) : Parcelable {

    override fun toString(): String {
        return "Movie(title='$title', id=$id, posterPath='$posterPath', overview='$overview', " +
                "releaseDate='$releaseDate', popularity=$popularity, voteAverage=$voteAverage)"
    }
}