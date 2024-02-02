package com.mbh.moviebrowser.features.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainException
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainFailure
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainSuccess
import com.mbh.moviebrowser.api.services.details.DetailsApiResponse
import com.mbh.moviebrowser.api.services.details.model.GenreDTO
import com.mbh.moviebrowser.api.services.details.model.MovieCollectionDTO
import com.mbh.moviebrowser.domain.data.MovieEntity
import com.mbh.moviebrowser.domain.usecase.FavoritesListUseCase
import com.mbh.moviebrowser.domain.usecase.SaveFavoriteMovieUseCase
import com.mbh.moviebrowser.domain.usecase.ViewMovieDetailsUseCase
import com.mbh.moviebrowser.features.destinations.DetailsScreenDestination
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val movieDto = DetailsApiResponse.MovieDetailDTO(
        adult = false,
        backdropPath = "/path/to/backdrop.jpg",
        collection = MovieCollectionDTO(
            id = 123,
            name = "Sample Collection",
            posterPath = "/path/to/collection_poster.jpg",
            backdropPath = "/path/to/collection_backdrop.jpg"
        ),
        budget = 10000000,
        genres = listOf(
            GenreDTO(id = "28", name = "Action"),
            GenreDTO(id = "12", name = "Adventure"),
            GenreDTO(id = "14", name = "Fantasy")
        ),
        homepage = "https://www.example.com",
        id = 123456,
        imdbId = "tt123456",
        originalLanguage = "en",
        originalTitle = "Sample Original Title",
        overview = "This is a sample movie overview.",
        popularity = 123.45,
        posterPath = "/path/to/poster.jpg",
        productionCompanies = emptyList(),
        productionCountries = emptyList(),
        releaseDate = "2023-01-01",
        revenue = 50000000,
        runtime = 120,
        spokenLanguages = emptyList(),
        status = "Released",
        tagline = "This is a sample tagline.",
        title = "Sample Movie Title",
        video = false,
        voteAverage = 7.5,
        voteCount = 1000
    )

    private val movie = MovieEntity.SAMPLE_MOVIE

    private val viewMovieDetailsUseCase = mockk<ViewMovieDetailsUseCase>()
    private val saveFavoriteMoviesUseCase = mockk<SaveFavoriteMovieUseCase>()
    private val favoritesListUseCase = mockk<FavoritesListUseCase>()

    val savedStateHandle = SavedStateHandle()

    lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        mockkObject(DetailsScreenDestination)
        coEvery { DetailsScreenDestination.argsFrom(savedStateHandle).movie } returns movie
        viewModel = DetailsViewModel(
            savedStateHandle = savedStateHandle,
            viewMovieDetailsUseCase = viewMovieDetailsUseCase,
            saveFavoriteMovieUseCase = saveFavoriteMoviesUseCase,
            favoritesListUseCase = favoritesListUseCase,
        )
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    //region: init and load data

    @Test
    fun `init and load data from repository as domain success`() = runTest {
        // given
        coEvery { viewMovieDetailsUseCase(any()) } returns domainSuccess(movie)
        coEvery { favoritesListUseCase() } returns flowOf(listOf(movie))
        // when
        viewModel.handle(Actions.Init)
        viewModel.state.test {
            // then
            awaitItem().let {
                assertThat(it.loading)
            }
            skipItems(1)
            awaitItem().let {
                assertThat(it.loading).isFalse()
                assertThat(it.movie).isNotNull()
                assertThat(it.favorite).isTrue()
            }
        }
        coVerify(exactly = 1) { viewMovieDetailsUseCase(movie.id) }
        coVerify(exactly = 2) { favoritesListUseCase() }
    }

    @Test
    fun `init and load data from repository as domain failure`() = runTest {
        // given
        coEvery { viewMovieDetailsUseCase(any()) } returns domainFailure("")
        coEvery { favoritesListUseCase() } returns flowOf(emptyList())
        // when
        viewModel.handle(Actions.Init)
        viewModel.state.test {
            skipItems(2)
            // then
            awaitItem().let {
                assertThat(it.loading).isFalse()
                assertThat(it.error)
            }
        }
        coVerify(exactly = 1) { viewMovieDetailsUseCase(movie.id) }
        coVerify(exactly = 1) { favoritesListUseCase() }
    }

    @Test
    fun `init and load data from repository as domain exception`() = runTest {
        // given
        coEvery { viewMovieDetailsUseCase(any()) } returns domainException(mockk())
        coEvery { favoritesListUseCase() } returns flowOf(emptyList())
        // when
        viewModel.handle(Actions.Init)
        viewModel.state.test {
            skipItems(2)
            // then
            awaitItem().let {
                assertThat(it.loading).isFalse()
                assertThat(it.error)
            }
        }
        coVerify(exactly = 1) { viewMovieDetailsUseCase(movie.id) }
        coVerify(exactly = 1) { favoritesListUseCase() }
    }

    // endregion

    // region: dismiss error

    @Test
    fun `dismiss error loads data`() = runTest {
        // given
        coEvery { viewMovieDetailsUseCase(any()) } returns domainException(mockk())
        coEvery { favoritesListUseCase() } returns flowOf(listOf())
        viewModel.handle(Actions.Init)
        // when
        viewModel.handle(Actions.DismissError)
        // then
        viewModel.state.test {
            awaitItem().let {
                assertThat(it.loading)
            }
            skipItems(2)
        }
        coVerify(exactly = 1) { viewMovieDetailsUseCase(movie.id) }
        coVerify(exactly = 1) { favoritesListUseCase() }
    }

    // endregion

    @Test
    fun `verify submitting favorite action to view model`() = runTest {
        // given
        coEvery { viewMovieDetailsUseCase(any()) } returns domainSuccess(movie)
        coEvery { favoritesListUseCase() } returns flowOf(listOf(movie))
        coEvery { saveFavoriteMoviesUseCase(any()) } returns Unit
        viewModel.handle(Actions.Init)
        // when
        viewModel.handle(Actions.Favorite)
        // then
        viewModel.state.test {
            skipItems(3)
        }
        coVerify(exactly = 1) { viewMovieDetailsUseCase(movie.id) }
        coVerify(exactly = 2) { favoritesListUseCase() }
        coVerify { saveFavoriteMoviesUseCase(movie) }
    }

}