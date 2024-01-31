package com.mbh.moviebrowser.api

import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainException
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainFailure
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainSuccess
import com.mbh.moviebrowser.api.services.details.DetailsApiResponse
import com.mbh.moviebrowser.api.services.details.DetailsDataSource
import com.mbh.moviebrowser.api.services.trending.TrendingApiResponse.TrendingMoviesDTO
import com.mbh.moviebrowser.api.services.trending.TrendingDataSource
import com.mbh.moviebrowser.api.services.trending.TrendingTimeWindow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TmdbRepository @Inject constructor(
    private val trendingDataSource: TrendingDataSource,
    private val detailsDataSource: DetailsDataSource,
) {

    suspend fun trendingMovies(period: TrendingTimeWindow) = try {
        trendingDataSource.request(period).map {
            if (it is TrendingMoviesDTO) domainSuccess(it.results)
            else domainFailure()
        }.first()
    } catch (e: Exception) {
        domainException(e)
    }

    suspend fun movieDetail(id: Int) = try {
        detailsDataSource.request(id).map {
            if (it is DetailsApiResponse.MovieDetailDTO) domainSuccess(it)
            else domainFailure()
        }.first()
    } catch (e: Exception) {
        domainException(e)
    }

    fun isFavorite(id: Int): Boolean {
        return false
    }

}