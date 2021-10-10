package org.sco.movieratings.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class MovieSchema(
    @PrimaryKey var id: Int,
    var title: String,
    var posterPath: String,
    var overview: String,
    var releaseDate: String,
    var popularity: Double,
    var voteAverage: Float
) : Parcelable
