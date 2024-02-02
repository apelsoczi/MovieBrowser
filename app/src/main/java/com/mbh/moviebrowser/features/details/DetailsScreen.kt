package com.mbh.moviebrowser.features.details

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mbh.moviebrowser.domain.data.MovieEntity
import com.mbh.moviebrowser.features.details.components.MovieAudienceContainer
import com.mbh.moviebrowser.features.details.components.MovieHeroBanner
import com.mbh.moviebrowser.features.details.components.MoviePlotContainer
import com.mbh.moviebrowser.ui.containers.ErrorContent
import com.mbh.moviebrowser.ui.shimmer.shimmerBrush
import com.ramcosta.composedestinations.annotation.Destination

data class DetailsScreenNavArgs(
    val movie: MovieEntity
)

@Destination(
    navArgsDelegate = DetailsScreenNavArgs::class
)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    var initialized by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(initialized) {
        if (initialized.not()) {
            viewModel.handle(Actions.Init)
            initialized = true
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()
    DetailsScreenContent(
        loading = state.value.loading,
        error = state.value.error,
        movie = state.value.movie,
        isFavorite = state.value.favorite,
        handle = { viewModel.handle(it) },
    )
}

@Composable
private fun DetailsScreenContent(
    loading: Boolean,
    error: Boolean,
    movie: MovieEntity?,
    isFavorite: Boolean?,
    handle: (Actions) -> Unit,
) {
    if (error) {
        ErrorContent(
            title = "There was an error",
            onDismissClick = { handle(Actions.DismissError) },
        )
        return
    } else {
        movie?.let {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(shimmerBrush(loading)),
            ) {
                MovieHeroBanner(
                    backdropUrl = movie.backdropUrl,
                    coverUrl = movie.coverUrl,
                    title = movie.title,
                    year = movie.releaseDate,
                )
                MovieAudienceContainer(
                    rating = movie.rating,
                    formattedRating = movie.formattedRating,
                    adult = movie.adult,
                    genres = movie.genres,
                )
                MoviePlotContainer(
                    favorite = isFavorite,
                    tagline = movie.tagline,
                    overview = movie.overview,
                    handle = handle,
                )
            }
        }
    }
}

@Composable
@Preview(
    name = "phone",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
fun DetailsScreenPreview() {
    DetailsScreenContent(
        loading = false,
        error = false,
        movie = MovieEntity.SAMPLE_MOVIE,
        isFavorite = false,
        handle = {},
    )
}
