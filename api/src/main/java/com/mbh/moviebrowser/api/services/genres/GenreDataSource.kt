package com.mbh.moviebrowser.api.services.genres

import com.mbh.moviebrowser.api.common.ApiServiceConsumer
import com.mbh.moviebrowser.api.common.DataResponseMapper
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainFailure
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GenreDataSource @Inject constructor(
    private val genreService: GenreService,
    private val json: Json,
    ioDispatcher: CoroutineDispatcher,
) : ApiServiceConsumer<GenreApiResponse>(
    ioDispatcher = ioDispatcher,
) {

    override val mapper = object : DataResponseMapper<GenreApiResponse>() {

        override fun decodeSuccess(bodyString: String): GenreApiResponse.GenresListDTO = with(json) {
            decodeFromString<GenreApiResponse.GenresListDTO>(bodyString)
        }.also {
            addToCache(domainSuccess(it))
        }

        override fun decodeError(bodyString: String): GenreApiResponse.GenresErrorDTO = with(json) {
            decodeFromString<GenreApiResponse.GenresErrorDTO>(bodyString)
        }.also {
            addToCache(domainFailure(it.statusMessage))
        }
    }

    suspend fun cached() = cached {
        genreService.movie()
    }

}