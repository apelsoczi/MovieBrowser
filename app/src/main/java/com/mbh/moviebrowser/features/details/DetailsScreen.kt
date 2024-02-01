package com.mbh.moviebrowser.features.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
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
import com.mbh.moviebrowser.domain.Movie
import com.mbh.moviebrowser.domain.MovieDetail
import com.mbh.moviebrowser.features.details.components.MovieAudienceContainer
import com.mbh.moviebrowser.features.details.components.MovieHeroBanner
import com.mbh.moviebrowser.features.details.components.MoviePlotContainer
import com.mbh.moviebrowser.ui.containers.ErrorContent
import com.ramcosta.composedestinations.annotation.Destination

data class DetailsScreenNavArgs(
    val movie: Movie
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
        loading = state.value.isLoading,
        error = state.value.isError,
        movie = state.value.movie,
        handle = { viewModel.handle(it) }
    )
}

@Composable
private fun DetailsScreenContent(
    loading: Boolean,
    error: Boolean,
    movie: MovieDetail?,
    handle: (Actions) -> Unit,
) {
    if (error) {
        ErrorContent(
            title = "There was an error",
            onDismissClick = { handle(Actions.DismissError) },
        )
        return
    }

    if (movie != null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            MovieHeroBanner(
                loading = loading,
                backdropUrl = movie.backdropUrl,
                coverUrl = movie.coverUrl,
                title = movie.title,
                year = movie.releaseDate,
            )
            MovieAudienceContainer(
                loading = loading,
                rating = movie.rating,
                formattedRating = movie.formattedRating,
                adult = movie.adult,
                genres = movie.genres,
            )
            MoviePlotContainer(
                loading = loading,
                favorite = movie.isFavorite,
                tagline = movie.tagline,
                overview = movie.overview,
                handle = handle,
            )
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
        movie = MovieDetail(
            id = 572802,
            title = "Aquaman and the Lost Kingdom",
            tagline = "The tide is turning.",
            overview = "Black Manta, still driven by the need to avenge his father's death and wielding the power of the mythic Black Trident, will stop at nothing to take Aquaman down once and for all. To defeat him, Aquaman must turn to his imprisoned brother Orm, the former King of Atlantis, to forge an unlikely alliance in order to save the world from irreversible destruction.",
            genres = listOf("Action", "Adventure", "Comedy"),
            coverUrl = "/7lTnXOy0iNtBAdRP3TZvaKJ77F6.jpg",
            backdropUrl = "/4gV6FOT4mEF4JaOmurO1kQSQ0Zl.jpg",
            rating = 6.995f,
            formattedRating = "6.9",
            adult = true,
            isFavorite = false,
            releaseDate = "2023-12-20",
        ),
        handle = {},
    )
}
