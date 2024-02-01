package com.mbh.moviebrowser.api.services.genres.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreDTO(
    val id: Int,
    val name: String
)