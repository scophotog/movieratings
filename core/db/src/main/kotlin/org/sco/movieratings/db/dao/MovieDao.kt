package org.sco.movieratings.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.sco.movieratings.db.model.MovieSchema

@Dao
interface MovieDao {

    @Transaction
    @Query(value = """
        SELECT * FROM movies
    """)
    fun getFavorites(): Flow<List<MovieSchema>>

    @Transaction
    @Query(value = """
        SELECT * FROM movies WHERE id = :movieId
    """)
    fun findFavorite(movieId: Int): Flow<MovieSchema?>

    @Insert
    suspend fun addFavorite(movieSchema: MovieSchema)

    @Delete
    suspend fun removeFavorite(movieSchema: MovieSchema)
}