package com.mbh.moviebrowser.api.services.details.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCollectionDTO(
    val id: Int,
    val name: String,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("backdrop_path") val backdropPath: String
)

