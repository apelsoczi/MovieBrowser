package com.mbh.moviebrowser.domain.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoritesDao {

    @Query("SELECT * FROM movie_details")
    suspend fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM movie_details")
    fun all(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie_details WHERE :id LIKE id LIMIT 1")
    fun movie(id: Int): Flow<MovieEntity?>

    @Insert
    fun addFavorite(movie: MovieEntity)

    @Delete
    fun removeFavorite(movie: MovieEntity)

    /** delete everything */
    @Query("DELETE FROM movie_details")
    fun deleteAll()

}