package com.mbh.moviebrowser.api.services.trending.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDTO(
    val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
    val id: Int,
    val title: String,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("original_title") val originalTitle: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("media_type") val mediaType: String,
    @SerialName("genre_ids") val genreIds: List<Int>,
    val popularity: Double,
    @SerialName("release_date") val releaseDate: String,
    val video: Boolean,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int
)