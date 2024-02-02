package com.mbh.moviebrowser.api

import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainException
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainFailure
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainSuccess
import com.mbh.moviebrowser.api.services.details.DetailsApiResponse
import com.mbh.moviebrowser.api.services.details.DetailsDataSource
import com.mbh.moviebrowser.api.services.genres.GenreApiResponse.GenresListDTO
import com.mbh.moviebrowser.api.services.genres.GenreDataSource
import com.mbh.moviebrowser.api.services.trending.TrendingApiResponse.TrendingMoviesDTO
import com.mbh.moviebrowser.api.services.trending.TrendingDataSource
import com.mbh.moviebrowser.api.services.trending.TrendingTimeWindow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TmdbRepository @Inject constructor(
    private val trendingDataSource: TrendingDataSource,
    private val detailsDataSource: DetailsDataSource,
    private val genreDataSource: GenreDataSource,
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

    suspend fun requestGenres() = try {
        genreDataSource.cached().map {
            if (it is GenresListDTO) domainSuccess(it)
            else domainFailure()
        }.first()
    } catch (e: Exception) {
        domainException(e)
    }

}