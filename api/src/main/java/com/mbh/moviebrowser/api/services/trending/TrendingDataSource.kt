package com.mbh.moviebrowser.api.services.trending

import com.mbh.moviebrowser.api.common.ApiServiceConsumer
import com.mbh.moviebrowser.api.common.DataResponseMapper
import com.mbh.moviebrowser.api.services.trending.TrendingApiResponse.TrendingErrorDTO
import com.mbh.moviebrowser.api.services.trending.TrendingApiResponse.TrendingMoviesDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class TrendingDataSource @Inject constructor(
    private val trendingService: TrendingService,
    private val json: Json,
    ioDispatcher: CoroutineDispatcher,
) : ApiServiceConsumer<TrendingApiResponse>(
    ioDispatcher = ioDispatcher,
) {

    override val mapper = object : DataResponseMapper<TrendingApiResponse>() {

        override fun decodeSuccess(bodyString: String): TrendingMoviesDTO = with(json) {
            decodeFromString<TrendingMoviesDTO>(bodyString)
        }

        override fun decodeError(bodyString: String): TrendingErrorDTO = with(json) {
            decodeFromString<TrendingErrorDTO>(bodyString)
        }
    }

    suspend fun request(timeWindow: TrendingTimeWindow) = request {
        trendingService.movies(timeWindow)
    }

}
