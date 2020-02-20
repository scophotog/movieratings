package org.sco.movieratings.db

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log

private const val TAG = "MovieProvider"

const val CONTENT_AUTHORITY = "org.sco.movieratings.db.MovieProvider"

private const val MOVIES = 100

val CONTENT_AUTHORITY_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

class MovieProvider: ContentProvider() {

    private val uriMatcher by lazy { buildUriMatcher() }

    private fun buildUriMatcher() : UriMatcher {
        Log.d(TAG, "buildUriMatcher: starts")
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        matcher.addURI(CONTENT_AUTHORITY, MovieContract.TABLE_NAME, MOVIES)
        return matcher
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate: starts")
        return true
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            MOVIES -> MovieContract.CONTENT_TYPE
            else -> throw IllegalArgumentException("unknown Uri: $uri")
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val match = uriMatcher.match(uri)
        val queryBuilder = SQLiteQueryBuilder()

        when (match) {
            MOVIES -> queryBuilder.tables = MovieContract.TABLE_NAME
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        val db = MovieFavoritesDbHelper.getInstance(context!!).readableDatabase
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val match = uriMatcher.match(uri)
        val recordId: Long
        val returnUri: Uri

        when(match) {
            MOVIES -> {
                val db = MovieFavoritesDbHelper.getInstance(context!!).writableDatabase
                recordId = db.insert(MovieContract.TABLE_NAME, null, values)
                if (recordId != -1L) {
                    returnUri = MovieContract.buildUriFromId(recordId)
                } else {
                    throw SQLException("Failed to insert, Uri was $uri")
                }
            }
            else -> throw IllegalArgumentException("Unknown uri: $uri")
        }
        return returnUri
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.d(TAG, "update: called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "update: match is $match")
        val count: Int

        when(match) {
            MOVIES -> {
                val db = MovieFavoritesDbHelper.getInstance(context!!).writableDatabase
                count = db.update(MovieContract.TABLE_NAME, values, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Unknown uri: $uri")
        }
        Log.d(TAG, "Exiting update, returning $count")
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "delete: called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "delete: match is $match")

        val count: Int

        when(match) {
            MOVIES -> {
                val db = MovieFavoritesDbHelper.getInstance(context!!).writableDatabase
                count = db.delete(MovieContract.TABLE_NAME, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Unknown uri: $uri")
        }
        Log.d(TAG, "Exiting update, returning $count")
        return count
    }

}