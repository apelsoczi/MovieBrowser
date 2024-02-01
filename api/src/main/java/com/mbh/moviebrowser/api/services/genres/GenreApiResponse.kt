package com.mbh.moviebrowser.api.services.genres

import com.mbh.moviebrowser.api.services.genres.model.GenreDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface GenreApiResponse {

    @Serializable
    data class GenresListDTO(
        val genres: List<GenreDTO>
    ) : GenreApiResponse

    @Serializable
    data class GenresErrorDTO(
        @SerialName("status_code") val statusCode: Int,
        @SerialName("status_message") val statusMessage: String,
    ) : GenreApiResponse

}