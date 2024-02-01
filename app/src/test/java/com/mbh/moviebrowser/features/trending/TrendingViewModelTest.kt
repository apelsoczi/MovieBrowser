package com.mbh.moviebrowser.features.trending

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mbh.moviebrowser.api.TmdbRepository
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainException
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainFailure
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainSuccess
import com.mbh.moviebrowser.api.services.trending.model.MovieDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class TrendingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val repository = mockk<TmdbRepository>()
    private val movieDto = MovieDTO(
        adult = false,
        backdropPath = "/cnqwv5Uz3UW5f086IWbQKr3ksJr.jpg",
        id = 572802,
        title = "Aquaman and the Lost Kingdom",
        originalLanguage = "en",
        originalTitle = "Aquaman and the Lost Kingdom",
        overview = "Black Manta, still driven by the need to avenge his father's death and wielding the power of the mythic Black Trident, will stop at nothing to take Aquaman down once and for all. To defeat him, Aquaman must turn to his imprisoned brother Orm, the former King of Atlantis, to forge an unlikely alliance in order to save the world from irreversible destruction.",
        posterPath = "/7lTnXOy0iNtBAdRP3TZvaKJ77F6.jpg",
        mediaType = "movie",
        genres = listOf("Action", "Adventure", "Fantasy"),
        popularity = 6996.108,
        releaseDate = "2023-12-20",
        video = false,
        voteAverage = 7.015,
        voteCount = 1100
    )

    lateinit var viewModel: TrendingViewModel

    @Before
    fun setup() {
        viewModel = TrendingViewModel(
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
        val data = listOf(movieDto)
        coEvery { repository.trendingMovies(any()) } returns domainSuccess(data)
        // when
        viewModel.handle(Actions.Init)
        viewModel.state.test {
            // then
            awaitItem().let {
                assertThat(it.isLoading)
            }
            awaitItem().let {
                assertThat(it.items.first().id).isEqualTo(movieDto.id)
                assertThat(it.isLoading).isFalse()
            }
        }
        coVerify(exactly = 1) { repository.trendingMovies(any()) }
    }

    @Test
    fun `init and load data from repository as domain failure`() = runTest {
        // given
        coEvery { repository.trendingMovies(any()) } returns domainFailure("")
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
        coVerify(exactly = 1) { repository.trendingMovies(any()) }
    }

    @Test
    fun `init and load data from repository as domain exception`() = runTest {
        // given
        coEvery { repository.trendingMovies(any()) } returns domainException(mockk())
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
        coVerify(exactly = 1) { repository.trendingMovies(any()) }
    }

    // endregion

    // region: dismiss error

    @Test
    fun `dismiss error loads data`() = runTest {
        // given
        val data = listOf(movieDto)
        coEvery { repository.trendingMovies(any()) } returns domainException(mockk())
        viewModel.handle(Actions.Init)
        // when
        viewModel.handle(Actions.DismissError)
        // then
        viewModel.state.test {
            awaitItem().let {
                println(it.isLoading)
            }
            skipItems(1)
        }
        coVerify(exactly = 1) { repository.trendingMovies(any()) }
    }

    // endregion

}