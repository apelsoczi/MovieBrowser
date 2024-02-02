package com.mbh.moviebrowser.domain.usecase

import com.mbh.moviebrowser.api.TmdbRepository
import com.mbh.moviebrowser.api.common.DomainResult
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainSuccess
import com.mbh.moviebrowser.api.services.trending.TrendingTimeWindow
import com.mbh.moviebrowser.domain.data.MovieEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TrendingMoviesTodayUseCase @Inject constructor(
    private val tmdbRepository: TmdbRepository,
    private val formatMovieUseCase: FormatMovieUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(): DomainResult<List<MovieEntity>> {
        val resultDTO = tmdbRepository.trendingMovies(TrendingTimeWindow.DAY)
        return when (resultDTO) {
            is DomainResult.Success -> domainSuccess(formatMovieUseCase(resultDTO.data))
            is DomainResult.Failure -> resultDTO
            is DomainResult.Exception -> resultDTO
        }
    }

}