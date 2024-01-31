package com.mbh.moviebrowser.features.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.api.TmdbRepository
import com.mbh.moviebrowser.api.common.DomainResult
import com.mbh.moviebrowser.api.services.details.DetailsApiResponse
import com.mbh.moviebrowser.domain.Movie
import com.mbh.moviebrowser.domain.MovieDetail
import com.mbh.moviebrowser.features.destinations.DetailsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed class Actions {

    data object Init : Actions()

    data object Favorite : Actions()

    data object DismissError : Actions()

}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: TmdbRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsViewState())
    val state: StateFlow<DetailsViewState> = _state.asStateFlow()

    fun handle(action: Actions) = when (action) {
        Actions.Init -> loadDetail()
        Actions.Favorite -> favorite()
        Actions.DismissError -> dismissError()
    }

    private fun loadDetail() {
        if (_state.value.isLoading) return

        _state.update {
            it.copy(
                isLoading = true
            )
        }

        val movie = DetailsScreenDestination.argsFrom(savedStateHandle).movie

        viewModelScope.launch {
            val result = repository.movieDetail(movie.id)
            when (result) {
                is DomainResult.Success -> _state.update {
                    it.copy(
                        isLoading = false,
                        isError = false,
                        movie = result.data.toDetailedMovie(movie)
                    )
                }
                is DomainResult.Failure -> _state.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                    )
                }
                is DomainResult.Exception -> _state.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                    )
                }
            }
        }
    }

    private fun favorite() {

    }

    private fun dismissError() {
        _state.update {
            it.copy(
                isError = false,
            )
        }
        loadDetail()
    }

    private fun DetailsApiResponse.MovieDetailDTO.toDetailedMovie(movie: Movie): MovieDetail {
        return MovieDetail(
            id = movie.id,
            title = title,
            tagline = tagline,
            overview = overview,
            coverUrl = movie.coverUrl,
            backdropUrl = "https://image.tmdb.org/t/p/original/${backdropPath}",
            rating = voteAverage.toFloat() / 10.0f,
            formattedRating = voteAverage.toString().substring(0, voteAverage.toString()
                .indexOf('.') + 2).replace(".0", ""),
            adult = adult,
            isFavorite = false,
            releaseDate = LocalDate.parse(releaseDate).year.toString(),
        )
    }

}
