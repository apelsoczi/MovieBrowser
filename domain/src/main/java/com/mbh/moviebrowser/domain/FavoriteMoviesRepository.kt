package com.mbh.moviebrowser.domain

import com.mbh.moviebrowser.domain.data.FavoritesDao
import com.mbh.moviebrowser.domain.data.MovieEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteMoviesRepository @Inject constructor(
    private val favoritesDao: FavoritesDao,
    private val ioDispatcher: CoroutineDispatcher,
) {

    fun favorites() = favoritesDao.all()

    fun getFavoriteMovie(id: Int): Flow<MovieEntity?> {
        return favoritesDao.movie(id)
    }

    suspend fun isFavorite(id: Int): Boolean {
        return getFavoriteMovie(id).first() != null
    }

    suspend fun saveFavorite(movie: MovieEntity) = withContext(ioDispatcher) {
        favoritesDao.addFavorite(movie)
    }

    suspend fun deleteFavorite(movie: MovieEntity) = withContext(ioDispatcher) {
        favoritesDao.removeFavorite(movie)
    }

}