package com.mbh.moviebrowser.api.services.details

import com.mbh.moviebrowser.api.common.ApiServiceConsumer
import com.mbh.moviebrowser.api.common.DataResponseMapper
import com.mbh.moviebrowser.api.services.details.DetailsApiResponse.DetailsErrorDTO
import com.mbh.moviebrowser.api.services.details.DetailsApiResponse.MovieDetailDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DetailsDataSource @Inject constructor(
    private val moviesService: MoviesService,
    private val json: Json,
    ioDispatcher: CoroutineDispatcher,
) : ApiServiceConsumer<DetailsApiResponse>(
    ioDispatcher = ioDispatcher,
) {

    override val mapper = object : DataResponseMapper<DetailsApiResponse>() {

        override fun decodeSuccess(bodyString: String) = with(json) {
            decodeFromString<MovieDetailDTO>(bodyString)
        }

        override fun decodeError(bodyString: String) = with(json) {
            decodeFromString<DetailsErrorDTO>(bodyString)
        }
    }

    suspend fun request(id: Int) = request {
        moviesService.detail(id.toString())
    }

}
