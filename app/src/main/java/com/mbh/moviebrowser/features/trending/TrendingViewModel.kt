package com.mbh.moviebrowser.features.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.api.TmdbRepository
import com.mbh.moviebrowser.api.common.DomainResult.Exception
import com.mbh.moviebrowser.api.common.DomainResult.Failure
import com.mbh.moviebrowser.api.common.DomainResult.Success
import com.mbh.moviebrowser.api.services.trending.TrendingTimeWindow
import com.mbh.moviebrowser.api.services.trending.model.MovieDTO
import com.mbh.moviebrowser.domain.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class Actions {

    data class ClickMovie(val movie: Movie) : Actions()

    data object Init : Actions()

    data object DismissError : Actions()

}

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val repository: TmdbRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(TrendingViewState())
    val state: StateFlow<TrendingViewState> = _state.asStateFlow()

    fun handle(init: Actions) = when (init) {
        is Actions.Init -> loadMovies()
        is Actions.ClickMovie -> {}
        is Actions.DismissError -> dismissError()
    }

    private fun loadMovies() {
        if (_state.value.isLoading) return

        _state.update {
            it.copy(
                isLoading = true,
            )
        }
        viewModelScope.launch {
            val result = repository.trendingMovies(TrendingTimeWindow.DAY)
            when (result) {
                is Success -> _state.update {
                    it.copy(
                        isLoading = false,
                        isError = false,
                        items = result.data.mapToMovieItems(),
                    )
                }
                is Failure -> _state.update {
                    it.copy(
                        isLoading = false,
                        isError = false,
                    )
                }
                is Exception -> _state.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                    )
                }
            }
        }
    }

    private fun dismissError() {
        _state.update {
            it.copy(
                isError = false,
            )
        }
        loadMovies()
    }

    private fun List<MovieDTO>.mapToMovieItems(): List<Movie> = map {
        Movie(
            id = it.id,
            title = it.title,
            genres = it.genreIds.joinToString(prefix = "[", postfix = "]"),
            overview = it.overview,
            coverUrl = "https://image.tmdb.org/t/p/w500/${it.posterPath}",
            rating = it.voteAverage.toFloat() / 10.0f,
            isFavorite = false,
        )
    }

}
