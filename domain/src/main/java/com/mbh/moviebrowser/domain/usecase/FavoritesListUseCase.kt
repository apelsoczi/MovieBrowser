package com.mbh.moviebrowser.domain.usecase

import com.mbh.moviebrowser.domain.FavoriteMoviesRepository
import com.mbh.moviebrowser.domain.data.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesListUseCase @Inject constructor(
    private val favoriteMoviesRepository: FavoriteMoviesRepository,
) {

    operator fun invoke(): Flow<List<MovieEntity>> = favoriteMoviesRepository.favorites()

}