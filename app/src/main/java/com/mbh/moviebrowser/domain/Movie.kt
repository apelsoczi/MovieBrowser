package com.mbh.moviebrowser.domain

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val genres: List<String>,
    val overview: String,
    val coverUrl: String,
    val rating: Float,
    val isFavorite: Boolean,
)
