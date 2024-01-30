package com.mbh.moviebrowser.api.services.trending

import com.mbh.moviebrowser.api.services.trending.model.MovieDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface TrendingApiResponse {

    @Serializable
    data class TrendingMoviesDTO(
        val page: Int,
        val results: List<MovieDTO>,
        @SerialName("total_pages") val totalPages: Int,
        @SerialName("total_results") val totalResults: Int
    ) : TrendingApiResponse

    @Serializable
    data class TrendingErrorDTO(
        @SerialName("status_code") val statusCode: Int,
        @SerialName("status_message") val statusMessage: String,
    ) : TrendingApiResponse

}
