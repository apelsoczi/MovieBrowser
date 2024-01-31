package com.mbh.moviebrowser.features.trending

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mbh.moviebrowser.domain.Movie
import com.mbh.moviebrowser.features.destinations.DetailsScreenDestination
import com.mbh.moviebrowser.features.trending.components.MovieList
import com.mbh.moviebrowser.ui.containers.ErrorContent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun TrendingScreen(
    viewModel: TrendingViewModel = hiltViewModel(),
    navController: DestinationsNavigator,
) {
    var initialized by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(initialized) {
        if (initialized.not()) {
            viewModel.handle(Actions.Init)
            initialized = true
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()
    TrendingScreenContent(
        loading = state.value.isLoading,
        error = state.value.isError,
        movies = state.value.items,
        handle = {
            when (it) {
                is Actions.ClickMovie -> navController.navigate(
                    DetailsScreenDestination.invoke(it.movie)
                )
                else -> viewModel.handle(it)
            }
        }
    )
}

@Composable
fun TrendingScreenContent(
    loading: Boolean,
    error: Boolean,
    movies: List<Movie>,
    handle: (Actions) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (error) {
            ErrorContent(
                title = "There was an error",
                onDismissClick = { handle(Actions.DismissError) },
            )
        } else {
            MovieList(
                loading = loading,
                movies = movies,
                onDetailsClicked = { handle(Actions.ClickMovie(it)) },
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
fun TrendingScreenContentPreview() {
    TrendingScreenContent(
        loading = false,
        error = false,
        movies = listOf(
            Movie(
                id = 455476,
                title = "Knights of the Zodiac",
                genres = "Action, Sci-fi",
                overview = "When a headstrong street orphan, Seiya, in search of his abducted sister unwittingly taps into hidden powers, he discovers he might be the only person alive who can protect a reincarnated goddess, sent to watch over humanity. Can he let his past go and embrace his destiny to become a Knight of the Zodiac?",
                coverUrl = "https://image.tmdb.org/t/p/w500/qW4crfED8mpNDadSmMdi7ZDzhXF.jpg",
                rating = 6.5f,
                isFavorite = true,
            ),
            Movie(
                id = 385687,
                title = "Fast X",
                genres = "Action",
                overview = "Over many missions and against impossible odds, Dom Toretto and his family have outsmarted, out-nerved and outdriven every foe in their path. Now, they confront the most lethal opponent they've ever faced: A terrifying threat emerging from the shadows of the past who's fueled by blood revenge, and who is determined to shatter this family and destroy everything—and everyone—that Dom loves, forever.",
                coverUrl = "https://image.tmdb.org/t/p/w500/fiVW06jE7z9YnO4trhaMEdclSiC.jpg",
                rating = 7.4f,
                isFavorite = false,
            ),
        ),
        handle = {},
    )
}
