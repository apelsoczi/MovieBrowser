package com.mbh.moviebrowser.domain.di

import android.content.Context
import androidx.room.Room
import com.mbh.moviebrowser.domain.data.FavoritesDao
import com.mbh.moviebrowser.domain.data.TmdbDatabase
import com.mbh.moviebrowser.domain.data.TmdbDatabase.Companion.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): TmdbDatabase {
        val builder = Room
            .databaseBuilder(context, TmdbDatabase::class.java, DB_NAME)
        return builder.build()
    }

    @Provides
    fun provideFavoritesDao(
        database: TmdbDatabase
    ): FavoritesDao {
        return database.foavoritesDao()
    }

}