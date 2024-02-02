package com.mbh.moviebrowser.features.trending

import com.mbh.moviebrowser.domain.data.MovieEntity

data class TrendingViewState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val items: List<MovieEntity> = emptyList(),
    val favorites: List<MovieEntity> = emptyList(),
)