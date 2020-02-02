package org.sco.movieratings.api.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Preview(var id: String, var iso_6391: String, var iso_3166_1: String, var key: String,
              var name: String, var site: String, var size: Int, var type: String) : Parcelable {

    override fun toString(): String {
        return "$site $key $name"
    }
}