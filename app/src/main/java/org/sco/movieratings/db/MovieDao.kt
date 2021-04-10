package org.sco.movieratings.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieSchema")
    fun getFavorites(): Flow<List<MovieSchema>>

    @Query("SELECT * FROM MovieSchema WHERE id = :movieId")
    fun findFavorite(movieId: Int): Flow<MovieSchema?>

    @Insert
    suspend fun addFavorite(movieSchema: MovieSchema)

    @Delete
    suspend fun removeFavorite(movieSchema: MovieSchema)
}