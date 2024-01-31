package com.mbh.moviebrowser.features.details

import com.mbh.moviebrowser.domain.MovieDetail

data class DetailsViewState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val movie: MovieDetail? = null,
)