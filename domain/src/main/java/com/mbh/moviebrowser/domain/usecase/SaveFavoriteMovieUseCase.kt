package com.mbh.moviebrowser.domain.usecase

import com.mbh.moviebrowser.domain.FavoriteMoviesRepository
import com.mbh.moviebrowser.domain.data.MovieEntity
import javax.inject.Inject

class SaveFavoriteMovieUseCase @Inject constructor(
    private val favoritesRepository: FavoriteMoviesRepository,
) {

    suspend operator fun invoke(movie: MovieEntity) {
        with(favoritesRepository) {
            if (isFavorite(movie.id)) deleteFavorite(movie)
            else saveFavorite(movie)
        }
    }

}