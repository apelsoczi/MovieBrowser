package com.mbh.moviebrowser.features.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.mbh.moviebrowser.api.TmdbRepository
import com.mbh.moviebrowser.api.common.DomainResult
import com.mbh.moviebrowser.api.services.details.DetailsApiResponse
import com.mbh.moviebrowser.api.services.details.model.GenreDTO
import com.mbh.moviebrowser.api.services.details.model.MovieCollectionDTO
import com.mbh.moviebrowser.domain.Movie
import com.mbh.moviebrowser.features.destinations.DetailsScreenDestination
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val repository = mockk<TmdbRepository>()
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
            GenreDTO(id = 28, name = "Action"),
            GenreDTO(id = 12, name = "Adventure"),
            GenreDTO(id = 14, name = "Fantasy")
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

    private val movie = Movie(
        id = movieDto.id,
        title = movieDto.title,
        genres = listOf("Action", "Adventure", "Fantasy"),
        overview = movieDto.overview,
        coverUrl = "https://image.tmdb.org/t/p/w500/${movieDto.posterPath}",
        rating = movieDto.voteAverage.toFloat(),
        isFavorite = false,
    )

    val savedStateHandle = SavedStateHandle()

    lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        mockkObject(DetailsScreenDestination)
        coEvery { DetailsScreenDestination.argsFrom(savedStateHandle).movie } returns movie
        viewModel = DetailsViewModel(
            savedStateHandle = savedStateHandle,
            repository = repository,
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
        coEvery { repository.movieDetail(any()) } returns DomainResult.domainSuccess(movieDto)
        // when
        viewModel.handle(Actions.Init)
        viewModel.state.test {
            // then
            awaitItem().let {
                Truth.assertThat(it.isLoading)
            }
            awaitItem().let {
                assertThat(it.isLoading).isFalse()
                assertThat(it.movie?.id).isEqualTo(movieDto.id)
            }
        }
        coVerify(exactly = 1) { repository.movieDetail(movieDto.id) }
    }

    @Test
    fun `init and load data from repository as domain failure`() = runTest {
        // given
        coEvery { repository.movieDetail(any()) } returns DomainResult.domainFailure("")
        // when
        viewModel.handle(Actions.Init)
        viewModel.state.test {
            skipItems(1)
            // then
            awaitItem().let {
                assertThat(it.isLoading).isFalse()
                assertThat(it.isError)
            }
        }
        coVerify(exactly = 1) { repository.movieDetail(movieDto.id) }
    }

    @Test
    fun `init and load data from repository as domain exception`() = runTest {
        // given
        coEvery { repository.movieDetail(any()) } returns DomainResult.domainException(mockk())
        // when
        viewModel.handle(Actions.Init)
        viewModel.state.test {
            skipItems(1)
            // then
            awaitItem().let {
                assertThat(it.isLoading).isFalse()
                assertThat(it.isError)
            }
        }
        coVerify(exactly = 1) { repository.movieDetail(movieDto.id) }
    }

    // endregion

    // region: dismiss error

    @Test
    fun `dismiss error loads data`() = runTest {
        // given
        val data = listOf(movieDto)
        coEvery { repository.movieDetail(any()) } returns DomainResult.domainException(mockk())
        viewModel.handle(Actions.Init)
        // when
        viewModel.handle(Actions.DismissError)
        // then
        viewModel.state.test {
            awaitItem().let {
                assertThat(it.isLoading)
            }
            skipItems(1)
        }
        coVerify { repository.movieDetail(movieDto.id) }
    }

    // endregion

}