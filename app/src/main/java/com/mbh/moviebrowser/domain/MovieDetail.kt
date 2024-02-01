package com.mbh.moviebrowser.domain

data class MovieDetail(
    val id: Int,
    val title: String,
    val tagline: String,
    val overview: String,
    val genres: List<String>,
    val coverUrl: String,
    val backdropUrl: String,
    val rating: Float,
    val formattedRating: String,
    val adult: Boolean,
    val isFavorite: Boolean,
    val releaseDate: String,
)