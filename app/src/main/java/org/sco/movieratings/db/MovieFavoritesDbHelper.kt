package org.sco.movieratings.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "movieDatabase.db"
private const val DATABASE_VERSION = 1

class MovieFavoritesDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val sSQL = """CREATE TABLE ${MovieContract.TABLE_NAME} (
            ${MovieContract.MovieEntry.MOVIE_ID} INTEGER NOT NULL,
            ${MovieContract.MovieEntry.MOVIE_TITLE} TEXT NOT NULL,
            ${MovieContract.MovieEntry.OVERVIEW} TEXT NOT NULL,
            ${MovieContract.MovieEntry.POSTER_PATH} TEXT NOT NULL,
            ${MovieContract.MovieEntry.RELEASE_DATE} TEXT NOT NULL,
            ${MovieContract.MovieEntry.RATING} REAL NOT NULL,
            ${MovieContract.MovieEntry.POPULARITY} REAL NOT NULL,
            ${MovieContract.MovieEntry.IS_FAVORITE} INTEGER NOT NULL);""".replaceIndent(" ")
        db.execSQL(sSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @Volatile
        private var instance: MovieFavoritesDbHelper? = null

        fun getInstance(context: Context): MovieFavoritesDbHelper =
            instance ?: synchronized(this) {
                instance ?: MovieFavoritesDbHelper(context).also { instance = it }
            }
    }
}