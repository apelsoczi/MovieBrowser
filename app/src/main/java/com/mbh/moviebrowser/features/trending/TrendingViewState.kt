package com.mbh.moviebrowser.features.trending

import com.mbh.moviebrowser.domain.Movie

data class TrendingViewState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val items: List<Movie> = emptyList(),
)