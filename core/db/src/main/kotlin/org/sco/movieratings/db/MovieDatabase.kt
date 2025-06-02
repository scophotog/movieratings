package org.sco.movieratings.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.sco.movieratings.db.dao.MovieDao
import org.sco.movieratings.db.model.MovieSchema

@Database(entities = [MovieSchema::class], version = 1, exportSchema = true)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}