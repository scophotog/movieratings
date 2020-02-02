package org.sco.movieratings.api.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review(var id: String, var author: String, var content: String, var url: String) : Parcelable {

    override fun toString() : String {
        return url
    }
}