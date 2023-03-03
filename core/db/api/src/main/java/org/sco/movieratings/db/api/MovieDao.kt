package org.sco.movieratings.db.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieSchema")
    suspend fun getFavorites(): List<MovieSchema>

    @Query("SELECT * FROM MovieSchema WHERE id = :movieId")
    suspend fun findFavorite(movieId: Int): MovieSchema?

    @Insert
    suspend fun addFavorite(movieSchema: MovieSchema)

    @Delete
    suspend fun removeFavorite(movieSchema: MovieSchema)
}