package com.mbh.moviebrowser.api

import com.mbh.moviebrowser.api.common.DomainResult
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainException
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainFailure
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainSuccess
import com.mbh.moviebrowser.api.common.DomainResult.Success
import com.mbh.moviebrowser.api.services.details.DetailsApiResponse
import com.mbh.moviebrowser.api.services.details.DetailsDataSource
import com.mbh.moviebrowser.api.services.genres.GenreApiResponse.GenresListDTO
import com.mbh.moviebrowser.api.services.genres.GenreDataSource
import com.mbh.moviebrowser.api.services.trending.TrendingApiResponse.TrendingMoviesDTO
import com.mbh.moviebrowser.api.services.trending.TrendingDataSource
import com.mbh.moviebrowser.api.services.trending.TrendingTimeWindow
import com.mbh.moviebrowser.api.services.trending.model.MovieDTO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TmdbRepository @Inject constructor(
    private val trendingDataSource: TrendingDataSource,
    private val detailsDataSource: DetailsDataSource,
    private val genreDataSource: GenreDataSource,
) {

    suspend fun trendingMovies(period: TrendingTimeWindow): DomainResult<List<MovieDTO>> {
        val trendingDto = try {
            trendingDataSource.request(period).map {
                if (it is TrendingMoviesDTO) domainSuccess(it.results)
                else domainFailure()
            }.first()
        } catch (e: Exception) {
            domainException(e)
        }

        // update genre labels if possible
        return if (trendingDto is Success) {
            val genresDto = requestGenres()
            val moviesWithGenres = trendingDto.data.map {
                it.copy(genres = mapGenreIdToName(it.genres, genresDto))
            }
            domainSuccess(moviesWithGenres)
        } else {
            trendingDto
        }
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

    private suspend fun requestGenres() = try {
        genreDataSource.cached().map {
            if (it is GenresListDTO) domainSuccess(it)
            else domainFailure()
        }.first()
    } catch (e: Exception) {
        domainException(e)
    }

    private fun mapGenreIdToName(
        genreIds: List<String>,
        genresDto: DomainResult<GenresListDTO>
    ): List<String> {
        return if (genresDto is Success) {
            genresDto.data.genres
                .filter { genreIds.contains(it.id.toString()) }
                .map { it.name }
        } else {
            emptyList()
        }
    }

}