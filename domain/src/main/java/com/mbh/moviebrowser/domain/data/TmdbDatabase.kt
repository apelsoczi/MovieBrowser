package com.mbh.moviebrowser.domain.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [MovieEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TmdbDatabase : RoomDatabase() {

    abstract fun foavoritesDao(): FavoritesDao

    companion object {
        val DB_NAME = "favorite-movies-database"
    }

}