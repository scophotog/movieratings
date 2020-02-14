package org.sco.movieratings.db

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

object MovieContract {

    internal const val TABLE_NAME = "movie"

    @kotlin.jvm.JvmField
    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)
    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"

    object MovieEntry : BaseColumns {
        const val MOVIE_ID = "movie_id"
        const val MOVIE_TITLE = "movie_title"
        const val OVERVIEW = "overview"
        const val POSTER_PATH = "poster_path"
        const val RELEASE_DATE = "release_date"
        const val RATING = "vote_average"
        const val POPULARITY = "popularity"
        const val IS_FAVORITE = "is_favorite"
    }

    fun buildUriFromId(id: Long): Uri {
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }
}