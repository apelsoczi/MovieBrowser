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
import com.mbh.moviebrowser.domain.data.MovieEntity
import com.mbh.moviebrowser.features.destinations.DetailsScreenDestination
import com.mbh.moviebrowser.features.trending.components.MovieList
import com.mbh.moviebrowser.ui.containers.ErrorContent
import com.mbh.moviebrowser.ui.containers.LoadingScreen
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
        favorites = state.value.favorites,
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
    movies: List<MovieEntity>,
    favorites: List<MovieEntity>,
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
        } else if (loading) {
            LoadingScreen()
        } else {
            MovieList(
                movies = movies,
                favorites = favorites,
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
    val favoriteMovie = MovieEntity.SAMPLE_MOVIE.copy(id = 0, adult = true)
    TrendingScreenContent(
        loading = false,
        error = false,
        movies = listOf(
            favoriteMovie,
            MovieEntity.SAMPLE_MOVIE,
        ),
        favorites = listOf(favoriteMovie),
        handle = {},
    )
}
