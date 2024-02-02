package com.mbh.moviebrowser.features.details

import com.mbh.moviebrowser.domain.data.MovieEntity

data class DetailsViewState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val movie: MovieEntity? = null,
    val favorite: Boolean? = null,
)