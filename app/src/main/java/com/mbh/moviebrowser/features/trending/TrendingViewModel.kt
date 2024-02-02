package com.mbh.moviebrowser.features.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.api.common.DomainResult.Exception
import com.mbh.moviebrowser.api.common.DomainResult.Failure
import com.mbh.moviebrowser.api.common.DomainResult.Success
import com.mbh.moviebrowser.domain.data.MovieEntity
import com.mbh.moviebrowser.domain.usecase.FavoritesListUseCase
import com.mbh.moviebrowser.domain.usecase.TrendingMoviesTodayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class Actions {

    data class ClickMovie(val movie: MovieEntity) : Actions()

    data object Init : Actions()

    data object DismissError : Actions()

}

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val trendingMoviesUseCase: TrendingMoviesTodayUseCase,
    private val favoritesListUseCase: FavoritesListUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(TrendingViewState())
    val state: StateFlow<TrendingViewState> = _state.asStateFlow()

    fun handle(init: Actions) = when (init) {
        is Actions.Init -> init()
        is Actions.ClickMovie -> {}
        is Actions.DismissError -> dismissError()
    }

    private fun init() {
        observeFavoritesList()
        loadMovies()
    }

    private fun observeFavoritesList() {
        viewModelScope.launch {
            favoritesListUseCase()
                .dropWhile { _state.value.isLoading }
                .collect { favorites ->
                    _state.update {
                        it.copy(favorites = favorites)
                    }
                }
        }
    }

    private fun loadMovies() {
        if (_state.value.isLoading) return

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = trendingMoviesUseCase()
            when (result) {
                is Success -> _state.update {
                    it.copy(
                        isLoading = false,
                        isError = false,
                        items = result.data,
                        favorites = favoritesListUseCase().first()
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
        _state.update { it.copy(isError = false) }
        loadMovies()
    }

}
