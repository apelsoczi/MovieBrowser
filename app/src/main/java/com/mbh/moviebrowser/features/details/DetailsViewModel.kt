package com.mbh.moviebrowser.features.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.api.common.DomainResult
import com.mbh.moviebrowser.domain.usecase.FavoritesListUseCase
import com.mbh.moviebrowser.domain.usecase.SaveFavoriteMovieUseCase
import com.mbh.moviebrowser.domain.usecase.ViewMovieDetailsUseCase
import com.mbh.moviebrowser.features.destinations.DetailsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class Actions {

    data object Init : Actions()

    data object Favorite : Actions()

    data object DismissError : Actions()

}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val viewMovieDetailsUseCase: ViewMovieDetailsUseCase,
    private val saveFavoriteMovieUseCase: SaveFavoriteMovieUseCase,
    private val favoritesListUseCase: FavoritesListUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsViewState())
    val state: StateFlow<DetailsViewState> = _state.asStateFlow()

    val movieId = DetailsScreenDestination.argsFrom(savedStateHandle).movie.id

    fun handle(action: Actions) = when (action) {
        Actions.Init -> init()
        Actions.Favorite -> favorite()
        Actions.DismissError -> dismissError()
    }

    private fun init() {
        observeFavoritesList()
        loadDetail()
    }

    private fun observeFavoritesList() {
        viewModelScope.launch {
            favoritesListUseCase()
                .map { favorites -> favorites.map { it.id } }
                .map { ids -> ids.contains(movieId) }
                .collect { favorite ->
                    _state.update {
                        it.copy(favorite = favorite)
                    }
                }
        }
    }

    private fun loadDetail() {
        if (_state.value.loading) return

        _state.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = viewMovieDetailsUseCase(movieId)
            when (result) {
                is DomainResult.Success -> _state.update {
                    it.copy(
                        loading = false,
                        error = false,
                        movie = result.data,
                        favorite = favoritesListUseCase()
                            .map { favorites -> favorites.map { it.id } }
                            .map { ids -> ids.contains(movieId) }
                            .first()
                    )
                }
                is DomainResult.Failure -> _state.update {
                    it.copy(
                        loading = false,
                        error = true,
                    )
                }
                is DomainResult.Exception -> _state.update {
                    it.copy(
                        loading = false,
                        error = true,
                    )
                }
            }
        }
    }

    private fun favorite() {
        _state.update { it.copy(loading = true) }

        viewModelScope.launch {
            _state.value.movie?.let {
                saveFavoriteMovieUseCase(it)
            }
//            // let the ui animation run for a while
            delay(1200)
            _state.update { it.copy(loading = false) }
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = false) }
        loadDetail()
    }

}
