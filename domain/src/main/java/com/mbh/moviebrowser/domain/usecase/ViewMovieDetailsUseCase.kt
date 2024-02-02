package com.mbh.moviebrowser.domain.usecase

import com.mbh.moviebrowser.api.TmdbRepository
import com.mbh.moviebrowser.api.common.DomainResult
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainSuccess
import com.mbh.moviebrowser.domain.FavoriteMoviesRepository
import com.mbh.moviebrowser.domain.data.MovieEntity
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ViewMovieDetailsUseCase @Inject constructor(
    private val favoritesRepository: FavoriteMoviesRepository,
    private val tmdbRepository: TmdbRepository,
    private val formatMovieUseCase: FormatMovieUseCase,
) {

    suspend operator fun invoke(movieId: Int): DomainResult<MovieEntity> {
        return when (favoritesRepository.isFavorite(movieId)) {
            // get from local
            true -> {
                favoritesRepository.getFavoriteMovie(movieId)
                    .filterNotNull()
                    .map { domainSuccess(it) }
                    .first()
            }
            // get from api
            false -> {
                val resultDTO = tmdbRepository.movieDetail(movieId)
                when (resultDTO) {
                    is DomainResult.Success -> domainSuccess(formatMovieUseCase(resultDTO.data))
                    is DomainResult.Failure -> resultDTO
                    is DomainResult.Exception -> resultDTO
                }
            }
        }
    }

}